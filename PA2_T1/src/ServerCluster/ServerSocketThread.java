package ServerCluster;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JTextArea;

/**
 *
 * @author david
 */

public class ServerSocketThread extends Thread {
    
    private int port;
    private JTextArea messagesTextArea;
    
    private ServerSocket serverSocket;
    private Socket clientSocketToRequests;
    
    public ServerSocketThread(int port, JTextArea messagesTextArea) {
        this.port = port;
        this.messagesTextArea = messagesTextArea;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server is listening on port: " + port);
            while (true) {
                System.out.println("Server is accepting a new connection");
                // wait for a new connection/client
                clientSocketToRequests = serverSocket.accept();
                Pi piCalculator = new Pi(clientSocketToRequests, messagesTextArea);
                piCalculator.start();
            }
        } catch (IOException ex) {
            System.out.println("Couldn't start socket!");
        }
    }

}
