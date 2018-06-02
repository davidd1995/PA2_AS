package LoadBalancer;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class HeartBeatReceiverThread extends Thread {

    private Socket hbsocket;
    private BufferedReader in;
    private PrintWriter out;
    private List<ServerInfo> activeservers;
    private ReentrantLock rl;
    private int serverid;
    private int size;
    private int portserver;
    private String hostserver = "localhost";

    public HeartBeatReceiverThread(Socket hbsocket, List<ServerInfo> activeservers, ReentrantLock rl,int serverid) {
        this.hbsocket = hbsocket;
        this.activeservers = activeservers;
        this.rl = rl;
        this.serverid=serverid;
    }

    @Override
    public void run() {
        try {
            // socket´s output stream
            out = new PrintWriter(hbsocket.getOutputStream(), true);
            // socket's input stream
            in = new BufferedReader(new InputStreamReader(hbsocket.getInputStream()));
            out.println(serverid);
            //idserver = Integer.parseInt(in.readLine());
            System.out.println("Chegou msg do servidor com id atribuido: " + serverid);

            portserver = Integer.parseInt(in.readLine());
            size = Integer.parseInt(in.readLine());

            rl.lock();
            try {
                boolean flag = false;
                for (int i = 0; i < activeservers.size(); i++) {
                    if (activeservers.get(i).getId() == serverid) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {

                    activeservers.add(new ServerInfo(serverid, portserver, hostserver, size));
                    display("O servidor com id "+serverid + " está ligado");
                }
            } catch (Exception e) {

            } finally {
                rl.unlock();
            }
            while (true) {
                // wait for a message from the client
                String text = in.readLine();
                // null message? server down
                if (text == null) {
                    System.out.println("Servidor mensagem nula" + serverid);
                    // end of communication with this client
                    //j.append("Server " + id + " is down!\n");
                    display("O servidor com id "+serverid + " foi abaixo");
                    rl.lock();
                    try {
                        int index = -1;
                        for (int i = 0; i < activeservers.size(); i++) {
                            if (activeservers.get(i).getId() == serverid) {
                                index = i;
                                break;
                            }
                        }
                        System.out.println("SDFEATERGAERREG "+index);
                        if (index != -1) {
                            activeservers.remove(index);
                        }
                    } catch (Exception e) {

                    } finally {
                        rl.unlock();
                    }
                    return;
                }
                //j.append("Server " + id + " is " + text + "\n");
                System.out.println(activeservers.toString());
            }
        } catch (Exception e) {
        }
    }
    
    private void display(String message) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoadBalancer.gui.appendEventsMonitor(message);
            }
        });
    }
}
