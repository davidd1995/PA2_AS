
package LoadBalancer;

import LoadBalancer.HeartBeat;
import LoadBalancer.Monitor;

/**
 *
 * @author david
 */
public class LoadBalancerMain {
    public static void main(String[] args){
        Monitor MtThread = new Monitor();
        HeartBeat HbThread = new HeartBeat();
        
   
        MtThread.start();
        HbThread.start();
        
        try{
            
            MtThread.join();
            HbThread.join();
        }catch(Exception ex) {}
    }
}
