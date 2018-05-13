package LoadBalancer;

import ServerCluster.Server;
import java.util.ArrayList;
import java.util.List;
import Client.Client;

public class Monitor{
    public List<Server> activeservers = new ArrayList<>();
    //public List<Client> activeclients = new ArrayList<>();
    
    public void addServer(Server s){
        activeservers.add(s);
    }
    
    public void addClient(Client c){
        //activeclients.add(c);
    }
    
    public void rmServer(int id){
        
    }
}
