package org.subtitle.assistor.udp;

public class UDPController{
    private int port;
    private UDPUTF8Server server;

    public UDPController(int port){
        try {
            this.server = new UDPUTF8Server(port);
        } catch(java.net.SocketException e) {
            // logging
            System.out.println("Server is not running.");
        }
    }

    public void addSubscriber(UDPSubscriber subscriber){
        this.server.addSubscriber(subscriber);
    }

    public void start() {
        new Thread(this.server).start();
    }
}
