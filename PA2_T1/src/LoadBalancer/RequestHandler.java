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

    public RequestHandler(Socket sk, LoadBalancerAndMonitorGUI lbgui, Monitor monitor) {
        this.lbgui = lbgui;
        this.clientlbsocket = sk;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        try {
            inclient = new BufferedReader(new InputStreamReader(clientlbsocket.getInputStream()));
            outclient = new PrintWriter(clientlbsocket.getOutputStream(), true);
            while(true){
                //read requests from client
                //System.out.println("Estou à espera do request");
                String request = inclient.readLine();

                // null message?
                if (request != null)
                    allocateRequest(request);
                else
                    break;
            }
            // close everything
            clientlbsocket.close();
            outclient.close();
            inclient.close();
        } catch (IOException ex) {
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void allocateRequest(String request) throws IOException {
                
        /*Check servers load*/
        int index = monitor.getIndexOfMostFreeServer();
        System.out.println("indice do servidor free: "+index);
        if (index != -1) {
            int serverport = monitor.getServerPort(index);
            lbserversocket = new Socket("localhost", serverport);

            outserver = new PrintWriter(lbserversocket.getOutputStream(), true);
            inserver = new BufferedReader(new InputStreamReader(lbserversocket.getInputStream()));

            //depois de decidir o servidor, manda o id do servidor no pedido.
            //manda para a GUI o evento cliente- servidor- pedido
            display(request);

            //send request to specific server
            outserver.println(request);

            //receive answer from server
            String response;
            try {
                response = inserver.readLine();
                display(response);
                //return answer to client
                outclient.println(response);
            } catch (IOException ex) {
                Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            outserver.close();
            inserver.close();
            lbserversocket.close();
        } else {
            
            display(request);
            System.out.println("!!!!!!"+request);
            display("Servers are busy!");
            outclient.println("Servers are busy!");
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
