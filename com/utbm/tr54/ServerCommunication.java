package fr.utbm.tr54;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import lejos.utility.Delay;

public class ServerCommunication {

	final static int port = 3000; 
	final static int taille = 1024; 
	final static byte buffer[] = new byte[taille];
	static DatagramSocket socket;
	static String rawData;
	static DatagramPacket data;
	
	public static void init () {
		
		System.out.println("-- INIT");
		data = new DatagramPacket(buffer,buffer.length);
		
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			socket.close();
			e.printStackTrace();
		}
		
	}
	
	public static void setRawData(float speed, float position) {
		rawData = Float.toString(speed) + "," + Float.toString(position);
	}
	
	public static void connect() {
		try {			
			socket.receive(data); 
			
			System.out.println("DATA address: " + data.getAddress());
			Delay.msDelay(1000);

		} catch (SocketException e) {
			socket.close();
			e.printStackTrace();
		} catch (IOException e) {
			socket.close();
			e.printStackTrace();
		} 		
	}
	
	public static void send() {
		byte tempBuffer[] = rawData.getBytes();
		data.setData(tempBuffer, 0, tempBuffer.length);
		
		try {
			socket.send(data);
		} catch (IOException e) {
			socket.close();
			e.printStackTrace();
		}
	}
	
	public static void close() {
		socket.close();
	}
	
}
