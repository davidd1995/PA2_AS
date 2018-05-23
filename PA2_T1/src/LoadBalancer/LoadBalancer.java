package LoadBalancer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoadBalancer extends Thread {
    
    private static int lbport;
    private static LoadBalancerAndMonitorGUI gui;
    private static ServerSocket lbsocket;
    private static Socket clientsocket;
    private Monitor monitor;

    public LoadBalancer() {
        monitor = new Monitor();
        gui = new LoadBalancerAndMonitorGUI(this,monitor);
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
        while (true) {
            System.out.println("estou a aguardar connects");
            clientsocket = lbsocket.accept();
            System.out.println("pedido recebido");
            System.out.println(clientsocket.getPort());
            RequestHandler req = new RequestHandler(clientsocket, gui, monitor);
            req.start();
            System.out.println("pedido encaminhado");
            
        }
    }

    public void setLbport(int lbport) {
        LoadBalancer.lbport = lbport;
    }
    public static void main(String[] args){
        LoadBalancer lb = new LoadBalancer();
    }
}