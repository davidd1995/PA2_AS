
package ServerCluster;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author david
 */

public class ClientSocketThread extends Thread {

    private Socket clientSocket;
    private PrintWriter out;

    private String ipAddress;
    private int port;
    private int serverID;
    private int queueSize;
    private int portToRequest;

    public ClientSocketThread(String ipAddress, int port, int serverID, int queueSize, int portToRequest) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.serverID = serverID;
        this.queueSize = queueSize;
        this.portToRequest = portToRequest;
    }

    @Override
    public void run() {
        try {
            clientSocket = new Socket(ipAddress, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true); // socket's output stream
            String request = serverID + "|" + queueSize + "|" + portToRequest;
            out.println(request);
            clientSocket.close();
            out.close();
        } catch (IOException ex) {
            System.err.println("Couldn't get I/O for the connection to the host ");
        }
    }

}
