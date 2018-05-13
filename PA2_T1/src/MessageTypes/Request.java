package MessageTypes;

public class Request {
    private String request;
    private String answer;
    private int ClientID;
    private int serverID;
    
    public Request(int clientid, String request){
        this.ClientID=clientid;
        this.request= request;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public int getClientID() {
        return ClientID;
    }

    public void setClientID(int ClientID) {
        this.ClientID = ClientID;
    }

    public int getServerID() {
        return serverID;
    }

    public void setServerID(int serverID) {
        this.serverID = serverID;
    }

    @Override
    public String toString() {
        return "Request{" + "request=" + request + ", ClientID=" + ClientID + ", serverID=" + serverID + '}';
    }
    
    
}
