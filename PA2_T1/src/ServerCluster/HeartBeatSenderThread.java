package ServerCluster;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HeartBeatSenderThread extends Thread {

    private int hbreceiverport;
    private int serverid;
    private Socket socket;
    private PrintWriter out;
    
    public HeartBeatSenderThread(int hbreceiverport, int serverid) {
        this.hbreceiverport=hbreceiverport;
        this.serverid=serverid;
    }
    
    @Override
    public void run() {

        try {
            socket = new Socket("localhost", hbreceiverport);
            out = new PrintWriter(socket.getOutputStream(), true);
            while (true) {
                out.println("Server "+serverid+" está ativo");
                sleep(2000);
            }
        } catch (IOException ex) {
            Logger.getLogger(HeartBeatSenderThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(HeartBeatSenderThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
