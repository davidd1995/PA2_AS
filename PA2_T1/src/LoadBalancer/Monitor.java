package LoadBalancer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {

    private ReentrantLock rl = new ReentrantLock();
    private int hbport;
    private ServerSocket monitorlisteningsocket;
    private Socket serverhbsocket;
    private List<ServerInfo> activeservers = new ArrayList<>();

    public void rmServer(int id) {

    }

    public void addRequestToServer() {

    }

    public void setHbport(int hbport) {
        this.hbport = hbport;
    }

    public ReentrantLock getRl() {
        return rl;
    }
    
    public int getIndexOfMostFreeServer(){
        int index=0;
        double fator=1;
        rl.lock();
        try {
            for(int i = 0;i< activeservers.size();i++){
                if(activeservers.get(i).getActive_requests()/activeservers.get(i).getSize()<fator){
                    fator = activeservers.get(i).getActive_requests()/activeservers.get(i).getSize();
                    index=i;
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
                    HeartBeatReceiverThread hb = new HeartBeatReceiverThread(serverhbsocket,activeservers,rl);
                    hb.start();
                }
            }
        };
        hbThread.start();
    }
}
