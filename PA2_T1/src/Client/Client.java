package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class Client {

    private static final AtomicInteger clientidcounter = new AtomicInteger(0); 
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

    public Client() {
        clientid=clientidcounter.incrementAndGet(); 
        gui = new ClientGUI(this);
        gui.setVisible(true);         
    }
    
    public void setConnection() throws IOException {
        System.out.println("!!!!!!CC :: "+clientid);
        // open a connection with the lb
        // create a socket
        lbsocket = new Socket(lbip, lbport);
        // socket's output stream
        out = new PrintWriter(lbsocket.getOutputStream(), true);
        // socket's input stream
        in = new BufferedReader(new InputStreamReader(lbsocket.getInputStream()));
        
        System.out.println("Connection is established with the Loadbalancer");
    }
    
    public void sendRequest() throws IOException {    
        // send the request to the server and display on GUI
        request="Request: |"+Integer.toString(clientid)+'|'+Integer.toString(requestid)+'|'+"01|"+Integer.toString(precision)+'|'+Integer.toString(delay);
        System.out.println("Request to send:"+ request);
        out.println(request);
        display(request);
        
        // wait and display response from server
        String response = in.readLine();
        System.out.println(response);
        display(response);
        requestid++;
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

    @Override
    public String toString() {
        return "Client{" + "clientid=" + clientid + ", request=" + request + ", lbip=" + lbip + ", lbport=" + lbport + '}';
    }
    
    public static void main(String[] args){
        Client x = new Client();
    }
}