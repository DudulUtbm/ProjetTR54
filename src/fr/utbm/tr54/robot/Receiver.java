package fr.utbm.tr54.robot;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Receiver implements Runnable{
	float hostSpeed;
	float hostPosition;
	Thread receiverThread;
	int port;
	InetAddress address;
	DatagramSocket socket = null;
	DatagramPacket packet;	
	byte[] buf = new byte[256];
	
	
	public Receiver() throws IOException{
		address = InetAddress.getByName("255.255.255.255");
		socket = new DatagramSocket();
		packet = new DatagramPacket(buf, buf.length, 
	            address, 3000);
		socket.send(packet);
		packet = new DatagramPacket(buf, buf.length);
		socket.receive(packet);
		String received = new String(packet.getData(), 0, packet.getLength());
		System.out.println("Quote: " + received);
	}

	@Override
	public void run() {
		for(;;){
			try {
				receiveInfos();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void receiveInfos() throws IOException{
		socket.receive(packet);
		String received =  new String(packet.getData(), 0, packet.getLength());
		String[] parsedMessage = received.split(",");
		hostSpeed = Float.parseFloat(parsedMessage[0]);
		hostPosition = Float.parseFloat(parsedMessage[1]);
	}
	
	public float getHostSpeed(){
		return hostSpeed;
	}
	public float getHostPosition(){
		return hostPosition;
	}
	
	public void start(){
		if(receiverThread==null){
			receiverThread= new Thread(this);
			receiverThread.start();
		}
	}
}
