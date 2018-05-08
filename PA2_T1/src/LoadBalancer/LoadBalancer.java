/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LoadBalancer;
/**
 *
 * @author david
 */
public class LoadBalancer extends Thread {
  
    
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1);
            } catch ( Exception ex ) {}
        }   
    } 
}
