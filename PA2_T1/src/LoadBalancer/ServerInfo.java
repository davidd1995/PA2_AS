package LoadBalancer;

/**
 *
 * @author gilguilherme
 */
public class ServerInfo {

    private int id;
    private int port;
    private String host;
    private int active_requests;
    private final int size;
    private int threadId;

    public ServerInfo(int id, int port, String host, int size) {
        this.id = id;
        this.port = port;
        this.active_requests = 0;
        this.host = host;
        this.size = size;
        this.threadId = 0;
    }

    public int getId() {
        return id;
    }

    public int getSize() {
        return size;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActive_requests() {
        return active_requests;
    }

    public synchronized void incrementThreadId() {
        this.threadId++;
    }

    public synchronized void incrementRequests() {
        this.active_requests++;
    }

    public synchronized void decrementRequests() {
        System.out.println("decrementei!!!! no server id "+this.getId());
        this.active_requests--;
    }

    @Override
    public String toString() {
        return "ServerInfo{" + "id=" + id + ", port=" + port + ", host=" + host + ", active_requests=" + active_requests + ", size=" + size + ", threadId=" + threadId + '}';
    }
}
