package fr.utbm.tr54.robot;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import lejos.utility.Delay;

public class RobocomLeader {
	
	public static void main(String[] args) throws IOException {
		
		Pilot.init(75, 30, 65);
		Delay.msDelay(500);

		ServerCommunication.init();
		ServerCommunication.setRawData(0, 0);
		ServerCommunication.connect();
		
		while (true) {
			ServerCommunication.setRawData(Pilot.MAIN_SPEED, Pilot.POSITION);
			ServerCommunication.send();
		}
	}
}
