package ServerCluster;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private int serverId;
    private int queueSize;
    private int portToRequest;
    private ServerSocket serverSocket = null;
    private Socket clientSocket = null;
    private String request;

    public Server(int serverId) {
        this.serverId = serverId;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getPortToRequest() {
        return portToRequest;
    }

    public void setPortToRequest(int portToRequest) {
        this.portToRequest = portToRequest;
    }

    public String getRequest() {
        return request;
    }

    public void startServer() throws IOException {
        serverSocket = new ServerSocket(portToRequest);
        System.out.println("Server is listening on port: " + portToRequest);
        while (true) {
            System.out.println("Server is accepting a new connection");
            // wait for a new connection/client
            clientSocket = serverSocket.accept();
            // create a new thread to deal with the new client
            Pi compute = new Pi(clientSocket, request);
            System.out.println("Socket: " + clientSocket.getLocalPort());
            compute.start();
        }
    }
}
