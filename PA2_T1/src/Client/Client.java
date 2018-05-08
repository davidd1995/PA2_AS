package Client;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author david
 */

public class Client {
    private String ip;
    private int cID;
    private int reqID;
    private int port;

    public Client(int client, int request) {
        this.cID = client;
        this.reqID = request;
    }

    public boolean connectToSocket() {
        Socket clientSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true); // socket's output stream
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // socket's input stream
        } catch (UnknownHostException e) {
            System.err.println(ip+"não foi encontrado");
            return false;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + ip);
            return false;
        }

        try {
            clientSocket.close();
            out.close();
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Ligou ao Servidor");
        return true;
    }
    public void setIpAddress(String ipAddress) {
        this.ip = ipAddress;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void sendMessage(int precision, int delay, ClientGUI clientGUI) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientSocketThread(ip, port, cID, reqID, precision, delay, clientGUI).start();
                reqID+=1;
            }
        });
    }



}
