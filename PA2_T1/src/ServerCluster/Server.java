package ServerCluster;

import java.awt.EventQueue;
import javax.swing.JTextArea;

/**
 *
 * @author david
 */

public class Server {

    private int serverId;
    private int queueSize;
    private int portToRequest;

    public Server(int serverId) {
        this.serverId = serverId;
    }

    public void connectToMonitor(String ipAddress, int port) {
        new ClientSocketThread(ipAddress, port, serverId, queueSize, portToRequest).start();
    }

    public void connectToRequestsHandler(int port, JTextArea messagesTextArea) {
        portToRequest = port;
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ServerSocketThread(port, messagesTextArea).start();
            }
        });
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

}
