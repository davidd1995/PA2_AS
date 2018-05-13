package Client;

import MessageTypes.Request;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private int clientid;
    private String lbip;
    private int lbport;
    private Socket lbsocket;
    private PrintWriter out;
    private BufferedReader in;
    private Request messagerequest;
    private String request;
    private ClientGUI gui;

    public Client(int clientid) {
        this.clientid = clientid;
        gui = new ClientGUI(this);
        gui.setVisible(true);
    }

    public void startClient() throws IOException {

        // open a connection with the lb
        // create a socket
        lbsocket = new Socket(lbip, lbport);
        // socket's output stream
        out = new PrintWriter(lbsocket.getOutputStream(), true);
        // socket's input stream
        in = new BufferedReader(new InputStreamReader(lbsocket.getInputStream()));

        System.out.println("Connection is established with the Server");
        
        // send the message to the server
        messagerequest= new Request(this.clientid,this.request);
        out.println(messagerequest);
        gui.displayReqOrAns("Request: "+messagerequest.getRequest());
        
        // wait for answer
        String txt = in.readLine();
        gui.displayReqOrAns("Answer: "+txt);
        // print echo
        System.out.println("Client received echo: " + txt);
        // empty message -> close connection
        out.close();
        in.close();
        lbsocket.close();
        System.out.println("Client closed the connection");
        //System.exit(0);
    }

    public void setLbip(String lbip) {
        this.lbip = lbip;
    }

    public void setLbport(int lbport) {
        this.lbport = lbport;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}