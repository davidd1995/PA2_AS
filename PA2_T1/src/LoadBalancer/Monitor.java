package LoadBalancer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {

    public ReentrantLock rl = new ReentrantLock();
    private static int serverid=0;
    private int hbport;
    private ServerSocket monitorlisteningsocket;
    private Socket serverhbsocket;
    private List<ServerInfo> activeservers = new ArrayList<>();

    public void setHbport(int hbport) {
        this.hbport = hbport;
    }

    public ReentrantLock getRl() {
        return rl;
    }
    
    public void decreaseServerRequest(int index){
        activeservers.get(index).decrementRequests();
    }

    public void increaseServerRequest(int index) {
        activeservers.get(index).incrementRequests();
    }

    public int getIndexOfMostFreeServer(){
        int index=0;
        double fator=1;
        rl.lock();
        try {
            for(int i = 0;i< activeservers.size();i++){
                if(activeservers.get(i).getActive_requests()/activeservers.get(i).getSize()<fator){
                    fator = (double)activeservers.get(i).getActive_requests()/(double)activeservers.get(i).getSize();
                    index=i;
                    System.out.println("!!!!!!!!!!!getMostfreeserver index: "+index);
                }
            }
        } finally {
            rl.unlock();
        }
        if(fator ==1){
            return -1;
        }
        return index;
    }
    
    public int getServerPort(int index){
        return activeservers.get(index).getPort();
    }

    //lança a thread para escutar ligações ao monitor e outra para escutar os heartbeats de cada servidor
    public void listenHeartBeats() {
        Thread hbThread = new Thread() {
            public void run() {
                System.out.println("cheguei à função do monitor que lanca a thread HBrec");
                try {
                    monitorlisteningsocket = new ServerSocket(hbport);
                } catch (Exception e) {
                    //jTextArea2.append(e.getMessage() + "\n");
                }
                //jTextArea2.append("Heartbeat is listening on port: " + Integer.parseInt(jTextField2.getText()) + "\n");
                while (true) {
                    //jTextArea2.append("Heartbeat is waiting for a new connection\n");
                    // wait for a new connection/client
                    try {
                        serverhbsocket = monitorlisteningsocket.accept();
                        //jTextArea2.append("Server Connected\n");
                    } catch (IOException ex) {
                    }                                       
                    HeartBeatReceiverThread hb = new HeartBeatReceiverThread(serverhbsocket,activeservers,rl,serverid);
                    hb.start();
                    serverid++;
                }
            }
        };
        hbThread.start();
    }
}
