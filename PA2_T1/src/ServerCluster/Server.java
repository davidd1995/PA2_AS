package ServerCluster;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private int serverId;
    private int queueSize;
    private int portToRequest;
    private int hbreceiverport;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private String request;
    private ServerGUI gui;
    
    public Server() {
        //this.serverId = serverId;
        gui = new ServerGUI(this);
        gui.setVisible(true);
    }

    public void startServer() throws IOException {
        //Start sending heartbeats in background
        HeartBeatSenderThread hbsend = new HeartBeatSenderThread(this);
        hbsend.start();

        //start listen requests
        serverSocket = new ServerSocket(portToRequest);
        System.out.println("Server is listening on port: " + portToRequest);

        Thread receiverequests = new Thread() {
            public void run() {
                while (true) {
                    System.out.println("Server is accepting a new connection");
                    try {
                        // wait for a new connection/client
                        clientSocket = serverSocket.accept();
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    // create a new thread to deal with the new client
                    Pi compute = new Pi(clientSocket, gui);
                    System.out.println("Socket: " + clientSocket.getLocalPort());
                    compute.start();
                }
            }

        };
        receiverequests.start();
        
    }
    
    public int getPortmonitorlistening() {
        return hbreceiverport;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setPortHbReceiver(int hbreceiverport) {
        this.hbreceiverport = hbreceiverport;
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
    
    public static void main(String[] args){
        Server x = new Server();
    }
}
