package LoadBalancer;
/**
 *
 * @author david
 */
public class Monitor extends Thread{
    
    
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1);
            } catch ( Exception ex ) {}
        }   
    } 
}
