package org.subtitle.assistor.udp;
//import java.util.concurrent.


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

public class UDPUTF8Server implements Runnable{
    private boolean isRunning;
    private List<UDPSubscriber> subscribers;
    private DatagramSocket socket;
    private byte[] buf;


    public UDPUTF8Server(int port) throws java.net.SocketException{
        this.socket = new DatagramSocket(port);
        this.subscribers = new LinkedList();
    }

    public void pause() {
        this.isRunning = true;
    }

    public void resume() {
        this.isRunning = false;
    }

    public void addSubscriber(UDPSubscriber subscriber){
        if(!this.isRunning){
            this.subscribers.add(subscriber);
        }
    }

    @Override
    public void run() {
        this.isRunning = true;

        System.out.println("Listener activated start! Registered " + this.subscribers.size() + " listeners");
        while (this.isRunning) {
            try {
                this.buf = new byte[2048];

                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                int port = packet.getPort();

                this.socket.receive(packet);
                InetAddress address = packet.getAddress();
                packet = new DatagramPacket(buf, buf.length, address, port);
                String received  = new String(packet.getData(), 0, packet.getLength());
                subscribers.forEach(e -> e.onCommandReceived(received));
            } catch(java.io.IOException e) {
                // add logging
                System.out.println("SERVER: Error while receiving a new package.");
            }

        }
        socket.close();
    }

    public void start() {
        new Thread(this).start();
    }
}
