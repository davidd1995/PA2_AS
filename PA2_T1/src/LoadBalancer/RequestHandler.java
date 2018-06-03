package LoadBalancer;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestHandler extends Thread {

    private LoadBalancerAndMonitorGUI lbgui;
    private Monitor monitor;
    private Socket clientlbsocket;
    private Socket lbserversocket = null;
    private PrintWriter outclient = null;
    private BufferedReader inclient = null;
    private PrintWriter outserver = null;
    private BufferedReader inserver = null;
    private int clientid;

    public RequestHandler(Socket sk, LoadBalancerAndMonitorGUI lbgui, Monitor monitor,int clientid) {
        this.lbgui = lbgui;
        this.clientlbsocket = sk;
        this.monitor = monitor;
        this.clientid=clientid;
    }

    @Override
    public void run() {
        try {
            inclient = new BufferedReader(new InputStreamReader(clientlbsocket.getInputStream()));
            outclient = new PrintWriter(clientlbsocket.getOutputStream(), true);
            outclient.println(clientid);
            while (true) {
                //read requests from client
                //System.out.println("Estou à espera do request");
                String request = inclient.readLine();

                // null message?
                if (request != null) {
                    allocateRequest(request);
                } else {
                    break;
                }
            }
            // close everything
            clientlbsocket.close();
            outclient.close();
            inclient.close();
        } catch (IOException ex) {
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void allocateRequest(String request) throws IOException, InterruptedException {
        /*Check servers load*/

        monitor.rl.lock();
        int index = monitor.getIndexOfMostFreeServer();

        System.out.println("indice do servidor free: " + index);
        if (index != -1) {
            monitor.increaseServerRequest(index);
            monitor.rl.unlock();
            int serverport = monitor.getServerPort(index);
            int serverid = monitor.getServerId(index);
            lbserversocket = new Socket("localhost", serverport);

            outserver = new PrintWriter(lbserversocket.getOutputStream(), true);
            inserver = new BufferedReader(new InputStreamReader(lbserversocket.getInputStream()));

            //depois de decidir o servidor, manda o id do servidor no pedido.
            //manda para a GUI o evento cliente- servidor- pedido
            String[] parts = request.split("\\|");
            display(request);
            display("O pedido nº "+parts[2]+ " do cliente "+parts[1]+" foi encaminhado p/servidor "+serverid);

            //send request to specific server
            outserver.println(request);

            //receive answer from server
            String response;
            try {
                response = inserver.readLine();
                //Server is down
                if (response == null) {
                    display("O pedido nº "+parts[2]+ " do cliente "+parts[1]+" vai ser realocado");
                    Thread.sleep(1000);
                    allocateRequest(request);

                } else {
                    display(response);
                    //return answer to client
                    outclient.println(response);
                }

            } catch (IOException ex) {
                Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            outserver.close();
            inserver.close();
            lbserversocket.close();
            Thread.sleep(1000);
            monitor.decreaseServerRequest(index);
        } else {
            monitor.rl.unlock();
            String[] parts = request.split("\\|");
            display(request);
            display("O pedido nº "+parts[2]+ " do cliente "+parts[1]+" foi rejeitado");
            display("Result: |" + parts[1] + "|" + parts[2] + "|03|" + parts[4] + "|" + parts[5]);
            outclient.println("Result: |" + parts[1] + "|" + parts[2] + "|03|" + parts[4] + "|" + parts[5]);
        }
    }

    private void display(String message) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                lbgui.appendEvents(message);
            }
        });
    }
}
