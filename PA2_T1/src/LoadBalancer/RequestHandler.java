package LoadBalancer;

import MessageTypes.Request;
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
        this.lbgui= lbgui;
        this.clientlbsocket = sk;
        this.monitor=monitor;
    }

    @Override
    public void run() {
        try {
            outclient = new PrintWriter(clientlbsocket.getOutputStream(), true);
            inclient = new BufferedReader(new InputStreamReader(clientlbsocket.getInputStream()));
            
            //read request from client
            String request = inclient.readLine();
            
            // null message?
            if (request != null) {
                /*if server load...code for requests allocation*/
                lbserversocket = new Socket("", 3000);
                //depois de decidir o servidor, manda o id do servidor no pedido.
                request.setServerID(32);
                //manda para a GUI o evento cliente- servidor- pedido
                display(request.toString());
                
                outserver = new PrintWriter(lbserversocket.getOutputStream(), true);
                inserver = new BufferedReader(new InputStreamReader(lbserversocket.getInputStream()));
                
                //send request to specific server
                outserver.println(request.getRequest());
                
                //receive answer from server
                String response = inserver.readLine();
                
                request.setAnswer(response);
                display(request.toString());
                //return answer to client
                outclient.println(request);
            }
            // close everything
            clientlbsocket.close();
            outclient.close();
            inclient.close();
            outserver.close();
            inserver.close();
            lbserversocket.close();
        } catch (IOException ex) {
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void display(final String message) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                lbgui.appendEvents(message);
            }
        });
    }
}
