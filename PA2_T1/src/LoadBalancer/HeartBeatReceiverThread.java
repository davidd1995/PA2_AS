package LoadBalancer;

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
    private int idserver;
    private int size;
    private int portserver;
    private String hostserver = "localhost";

    public HeartBeatReceiverThread(Socket hbsocket, List<ServerInfo> activeservers, ReentrantLock rl) {
        this.hbsocket = hbsocket;
        this.activeservers = activeservers;
        this.rl = rl;
    }

    @Override
    public void run() {
        try {
            // socket´s output stream
            out = new PrintWriter(hbsocket.getOutputStream(), true);
            // socket's input stream
            in = new BufferedReader(new InputStreamReader(hbsocket.getInputStream()));

            idserver = Integer.parseInt(in.readLine());
            System.out.println("Chegou msg com id do servidor " + idserver);

            portserver = Integer.parseInt(in.readLine());
            size = Integer.parseInt(in.readLine());

            rl.lock();
            try {
                boolean flag = false;
                for (int i = 0; i < activeservers.size(); i++) {
                    if (activeservers.get(i).getId() == idserver) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    activeservers.add(new ServerInfo(idserver, portserver, hostserver, size));
                    System.out.println("Servidor ligado do lb" + idserver);
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
                    System.out.println("Servidor mensagem nula" + idserver);
                    // end of communication with this client
                    //j.append("Server " + id + " is down!\n");
                    rl.lock();
                    try {
                        int index = -1;
                        for (int i = 0; i < activeservers.size(); i++) {
                            if (activeservers.get(i).getId() == idserver) {
                                index = i;
                                break;
                            }
                        }
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
}
