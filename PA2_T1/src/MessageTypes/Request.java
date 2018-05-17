package MessageTypes;

public class Request {
    private int requestID;
    private int clientID;
    private int precision;
    private int delay;
    
    public Request(int clientid, int requestid, int precision, int delay){
        this.clientID=clientid;
        this.requestID= requestid;
        this.precision=precision;
        this.delay=delay;
    }

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public String toString() {
        return "Request{" + "requestID=" + requestID + ", clientID=" + clientID + ", precision=" + precision + ", delay=" + delay + '}';
    }
  
}
