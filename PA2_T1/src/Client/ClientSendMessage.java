/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.io.BufferedReader;
import java.io.PrintWriter;

/**
 *
 * @author gilguilherme
 */
public class ClientSendMessage {
        public String request;
    public PrintWriter out;
    public BufferedReader in;
    public ClientGUI clientGUI;
    public ClientSendMessage(String request,PrintWriter out, BufferedReader in, ClientGUI gui){
                this.request = request;
        this.out = out;
        this.in = in;
        this.clientGUI = clientGUI;
    }
    
    @Override
    public void run() {
        try {
            JTextArea messageArea = clientGUI.getMessageArea();
            JTextField precisionTextBox = clientGUI.getPrecisionTextBox();
            JTextField delayTextBox = clientGUI.getDelayTextBox();
            JButton sendButton = clientGUI.getSendButton();
            out.println(request);
            messageArea.setText(messageArea.getText()+ "Request to LB: " + request + "\n");
            precisionTextBox.setEditable(false);
            delayTextBox.setEditable(false);
            sendButton.setEnabled(false);
            String response = in.readLine();
            messageArea.setText(messageArea.getText() + "Response from LB: " + response + "\n");
            precisionTextBox.setEditable(true);
            delayTextBox.setEditable(true);
            sendButton.setEnabled(true);
        } catch (IOException ex) {
            System.err.println("Couldn't get I/O for the connection to the host ");
        }
    }
    
}
