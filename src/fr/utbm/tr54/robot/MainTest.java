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
import lejos.hardware.Button;
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
	private static boolean wait;

	public static void main(String[] args) throws IOException {
		wait = false;
		brick = BrickFinder.getLocal();
	 	final int button = Button.waitForAnyPress();
		boolean menu = true;
		
		while (menu){
			if(button == Button.ID_UP) {
				msgListener = new MessageListener(brick.getName(),true);
				menu = false;
			} else if(button == Button.ID_DOWN) {
				msgListener = new MessageListener(brick.getName(),false);
				menu = false;
			}
		}
		
		BroadcastReceiver.getInstance().addListener(msgListener);
		
		Pilot.init(50, 18, 32);

		LEDController.switchOff();

		SensorController sControl = new SensorController();
	 	sControl.start();
	 	LEDController.switchRed();
	 	
	 	
		Delay.msDelay(500);

		while(true){
			
			if(msgListener.isWaiting){
				if(msgListener.isCrossing){
					msgListener.isWaiting = false;
					wait = false;
				} else {
					//this function needs to be done : count number of wheels turn since orange mark is crossed
					//until a reference then return false which will trigger "stop" in the next condition
					wait = Pilot.wait();
				}
			}
			if(msgListener.isCrossing){
				if(System.currentTimeMillis() - msgListener.crossingTime > 5000){
					msgListener.currentRoute = !msgListener.currentRoute;
					msgListener.crossingTime = 0;
					msgListener.isCrossing = false;
					JSONObject obj = new JSONObject();
					try{
						obj.put("name", msgListener.name);
						obj.put("isCrossing",false);
						obj.put("isWaiting", false);
						obj.put("currentRoute", msgListener.currentRoute);
						obj.put("crossRequest", false);
						//first message sent for crossing the road
						BroadcastManager.getInstance().broadcast(obj.toString().getBytes());
					}catch(Exception e){
						
					}
				}
			}
			//remove duplicate color detection
			//we need to continue checking the distance even though color is the same
			while(prevColor == sControl.sample.getColor()){
				if(Pilot.distance(5) < 0.20f || wait){
					Pilot.stop();
					break;
				}
			}
			if(Pilot.distance(5) < 0.20f || wait){
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
								obj.put("crossRequest", true);
								//first message sent for crossing the road
								BroadcastManager.getInstance().broadcast(obj.toString().getBytes());
							}catch(Exception e){
								
							}
						}else if (isOrange && prevColor != Color.ORANGE){
							LEDController.switchGreen();
							isOrange = false;
							JSONObject obj = new JSONObject();
							try{
								obj.put("name", msgListener.name);
								obj.put("isCrossing",false);
								obj.put("isWaiting", false);
								obj.put("currentRoute", msgListener.currentRoute);
								obj.put("crossRequest", true);
								//first message sent for crossing the road
								BroadcastManager.getInstance().broadcast(obj.toString().getBytes());
							}catch(Exception e){
								
							}
							
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
