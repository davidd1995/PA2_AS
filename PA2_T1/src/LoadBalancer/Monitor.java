package LoadBalancer;

import MessageTypes.Request;
import ServerCluster.Server;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Monitor{
    private int hbport;
    private List<Server> activeservers = new ArrayList<>();
    private Map<Server,List<Request>> serverrequests = new HashMap<>();
    
    public void addServer(Server s){
        activeservers.add(s);
    }
    
    public void rmServer(int id){
        
    }
    
    public void addRequestToServer(){
     
    }

    public void setHbport(int hbport) {
        this.hbport = hbport;
    }
    //lança a thread para escutar os heartbeats
    public void listenHeartBeats() {
        HeartBeatReceiverThread hb = new HeartBeatReceiverThread(hbport);
        hb.start();
    }
    
}