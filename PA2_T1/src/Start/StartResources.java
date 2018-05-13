package Start;

import Client.Client;
import LoadBalancer.*;
import ServerCluster.*;

public class StartResources {

    public static void main(String[] args) {
        /*Monitor monit = new Monitor();
        LoadBalancer lb = new LoadBalancer();
        HeartBeat hb = new HeartBeat();

        for (int i = 0; i < 3; i++) {
            monit.addServer(new Server(i));
        }

        for (int i = 0; i < 3; i++) {
            monit.addClient(new Client(i));
        }*/
        Client x = new Client(3);
        Client y = new Client(2);    
    }
}
