package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private int clientid;
    private int requestid=1;
    private int precision;
    private int delay;
    private String request;
    private String lbip;
    private int lbport;
    private Socket lbsocket;
    private PrintWriter out;
    private BufferedReader in;
    private ClientGUI gui;

    public Client(int clientid) {
        this.clientid = clientid;
        gui = new ClientGUI(this);
        gui.setVisible(true);
    }
    
    public void setConnection() throws IOException {
        // open a connection with the lb
        // create a socket
        lbsocket = new Socket(lbip, lbport);
        // socket's output stream
        out = new PrintWriter(lbsocket.getOutputStream(), true);
        // socket's input stream
        in = new BufferedReader(new InputStreamReader(lbsocket.getInputStream()));
        
        System.out.println("Connection is established with the Server");
    }
    
    public void sendRequest() throws IOException {    
        // send the request to the server and display on GUI
        request=Integer.toString(clientid)+'|'+Integer.toString(requestid)+'|'+"01|"+Integer.toString(precision)+'|'+Integer.toString(delay);
        out.println(request);
        display("Request: "+request);
        
        // wait and display response from server
        String response = in.readLine();
        display("Response: "+response);
        requestid++;

        //out.close();
        //in.close();
        //lbsocket.close();
        //System.out.println("Client "+clientid+" closed the connection");
        //System.exit(0);
    }

    public void setLbip(String lbip) {
        this.lbip = lbip;
    }

    public void setLbport(int lbport) {
        this.lbport = lbport;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }
    
    public void setDelay(int delay) {
        this.delay = delay;
    }
    
    private void display(String message) {
        gui.displayReqOrAns(message);
    }
}