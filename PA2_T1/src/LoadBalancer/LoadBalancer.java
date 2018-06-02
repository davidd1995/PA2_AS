package LoadBalancer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoadBalancer extends Thread {

    private int lbport;
    public static LoadBalancerAndMonitorGUI gui;
    private ServerSocket lbsocket;
    private Socket clientsocket;
    private Monitor monitor;
    private static int clientid=0;

    public LoadBalancer() {
        monitor = new Monitor();
        gui = new LoadBalancerAndMonitorGUI(this, monitor);
        gui.setVisible(true);
    }

    public void startLbAndMonitor() throws IOException {

        try {
            lbsocket = new ServerSocket(lbport);
        } catch (IOException ex) {
            Logger.getLogger(LoadBalancer.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Start listening connections from servers and heartbeats in a thread
        monitor.listenHeartBeats();

        //Receive requests from clients and launch one thread for each one
        Thread receiverequests = new Thread() {
            public void run() {
                while (true) {
                    try {
                        clientsocket = lbsocket.accept();
                    } catch (IOException ex) {
                        Logger.getLogger(LoadBalancer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println("pedido recebido");
                    System.out.println(clientsocket.getPort());
                    RequestHandler req = new RequestHandler(clientsocket, gui, monitor,clientid);
                    req.start();
                    clientid++;
                    System.out.println("pedido encaminhado");

                }
            }
        };
        receiverequests.start();

    }

    public void setLbport(int lbport) {
        this.lbport = lbport;
    }

    public static void main(String[] args) {
        LoadBalancer lb = new LoadBalancer();
    }
}
