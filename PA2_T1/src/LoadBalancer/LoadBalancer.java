package LoadBalancer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoadBalancer extends Thread {
    
    private static int lbport;
    private static LoadBalancerGUI gui;
    private static ServerSocket lbsocket;
    private static Socket clientsocket;

    public LoadBalancer() {
        gui = new LoadBalancerGUI(this);
        gui.setVisible(true);
    }

    public void startLoadBalancer() throws IOException {
        try {
            LoadBalancer.lbsocket = new ServerSocket(lbport);
        } catch (IOException ex) {
            Logger.getLogger(LoadBalancer.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Receive requests from clients and launch one thread for each one
        while (true) {
            clientsocket = lbsocket.accept();
            RequestHandler req = new RequestHandler(clientsocket, gui);
            req.start();
        }
    }

    public void setLbport(int lbport) {
        this.lbport = lbport;
    }
}
