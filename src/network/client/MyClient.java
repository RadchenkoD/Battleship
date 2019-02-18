package network.client;

public interface MyClient {
    void sendRequest(Object request);
    Object receiveResponse();
    void connectToServer();
    void leaveServer();
}
