package LoadBalancer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HeartBeatReceiverThread extends Thread{
    private Socket socket;
    private ServerSocket HBSocket;
    private int hbport;
    private BufferedReader in;
    
    public HeartBeatReceiverThread(int hbport){
        this.hbport=hbport;
    }
    
    @Override
    public void run() {
        try {
            HBSocket = new ServerSocket(hbport);
        } catch (IOException ex) {
            Logger.getLogger(HeartBeatReceiverThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("HB is listening on port: " + hbport);
        while (true) {
            System.out.println("Server is accepting a new connection");
            try {
                // wait for a new connection/client
                socket = HBSocket.accept();
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            } catch (IOException ex) {
                Logger.getLogger(HeartBeatReceiverThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    } 
}
