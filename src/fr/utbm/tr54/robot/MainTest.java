package fr.utbm.tr54.robot;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.json.JSONObject;

import fr.utbm.tr54.network.BroadcastListener;
import fr.utbm.tr54.network.BroadcastManager;
import fr.utbm.tr54.network.BroadcastReceiver;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.BrickInfo;
import lejos.utility.Delay;

/**
 * Follow the track and stop before hitting obstacles (other robot)
 * @author rdulieu
 *
 */
public class MainTest {

	private static boolean isOrange=false;
	private static Color prevColor;
	private static Brick brick;
	private static MessageListener msgListener;
	

	public static void main(String[] args) throws IOException {
		brick = BrickFinder.getLocal();
		
		msgListener = new MessageListener(brick.getName(),isOrange);
		BroadcastReceiver.getInstance().addListener(msgListener);
		
		Pilot.init(50, 18, 32);

		LEDController.switchOff();

		SensorController sControl = new SensorController();
	 	sControl.start();
	 	LEDController.switchRed();

		Delay.msDelay(500);

		while(true){
			while(prevColor == sControl.sample.getColor());
			if(Pilot.distance(5) < 0.20f){
				Pilot.stop();
		
			}else if (Pilot.distance(5) >= 0.20f){
				
				if(sControl.sample.isBlue()){
					prevColor = Color.BLUE;
					//LEDController.switchGreen();
					Pilot.forward();
				}else{
					Pilot.set_speed(60);
					
					if(sControl.sample.isWhite()){
						prevColor = Color.WHITE;
						Pilot.turn_right();
						while(sControl.sample.isWhite()){
							Pilot.turn_right();
						}
						
					}else if(sControl.sample.isBlack()){
						prevColor = Color.BLACK;
						Pilot.turn_left();
						while(sControl.sample.isBlack()){
							Pilot.turn_left();
						}
						
					}else if(sControl.sample.isOrange()){
						Pilot.forward();

						if(!isOrange && prevColor != Color.ORANGE){
							LEDController.switchOrange();
							isOrange = true;
							JSONObject obj = new JSONObject();
							try{
								obj.put("name", msgListener.name);
								obj.put("isCrossing",false);
								obj.put("isWaiting", false);
								obj.put("currentRoute", msgListener.currentRoute);
								//first message sent for crossing the road
								BroadcastManager.getInstance().broadcast(obj.toString().getBytes());
							}catch(Exception e){
								
							}
						}else if (isOrange && prevColor != Color.ORANGE){
							LEDController.switchGreen();
							isOrange = false;
							
						}
		
						prevColor = Color.ORANGE;

					}else{
						Pilot.set_speed(50);
					}

				}
				
			}

		}
	}


}
