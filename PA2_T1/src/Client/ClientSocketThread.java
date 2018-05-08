
package Client;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author david
 */

public class ClientSocketThread extends Thread {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    private String ipAddress;
    private int port;
    private int clientID;
    private int requestID;
    private int precision;
    private int delay;
    private ClientGUI clientGUI;

    public ClientSocketThread(String ipAddress, int port, int clientID, int requestID, int precision, int delay, ClientGUI clientGUI) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.clientID = clientID;
        this.requestID = requestID;
        this.precision = precision;
        this.delay = delay;
        this.clientGUI = clientGUI;
    }

    @Override
    public void run() {
        try {
            clientSocket = new Socket(ipAddress, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true); // socket's output stream
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // socket's input stream
            String request = "request: |" + clientID + "|" + requestID + "|01|" + precision + "|" + delay + "|";
            out.println(request);
            display(request);
            String response = in.readLine();
            display(response);
            clientSocket.close();
            out.close();
            in.close();
            enable();
        } catch (IOException ex) {
            System.err.println("Couldn't get I/O for the connection to the host ");
        }
    }

    private void display(final String message) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                clientGUI.addMessage(message);
            }
        });
    }

    private void enable() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                clientGUI.getPrecisionTextBox().setEditable(true);
                clientGUI.getPrecisionTextBox().setText("");
                clientGUI.getDelayTextBox().setEditable(true);
                clientGUI.getDelayTextBox().setText("");
                clientGUI.getSendButton().setEnabled(true);
            }
        });
    }

}
