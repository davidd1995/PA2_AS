package ServerCluster;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HeartBeatSenderThread extends Thread {

    private Server server;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public HeartBeatSenderThread(Server server) {
        this.server= server;
    }

    @Override
    public void run() {
        try {
            // create a socket
            socket = new Socket("localhost", server.getPortmonitorlistening());
            // socket's output stream
            out = new PrintWriter(socket.getOutputStream(), true);
            // socket's input stream
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //jTextArea1.append("Connection is established with the Monitor\n");
            //jButton1.setEnabled(false);
            int id= Integer.parseInt(in.readLine());
            server.setServerId(id); 
            //jTextArea1.append("id = " + id + "\n");
            out.println(server.getPortToRequest());
            out.println(server.getQueueSize());
        } catch (UnknownHostException e) {
            //jTextArea1.append("Don't know about host\n");
        } catch (IOException e) {
            //jTextArea1.append("Couldn't get I/O for the connection to host\n");
        }
        while (true) {
            out.println("Alive!");
            try {
                sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
