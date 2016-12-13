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

	private static boolean currentRoute=false;
	private static Color prevColor;
	private static Brick brick;
	private static MessageListener msgListener;
	private static boolean wait;
	private static long startTime;
	private static long lastOrangeDetectionTime;

	public static void main(String[] args) throws IOException {
		
		startTime = System.currentTimeMillis();
		wait = false;
		brick = BrickFinder.getLocal();
		lastOrangeDetectionTime = System.currentTimeMillis();
	 	//final int button = Button.waitForAnyPress();
		boolean menu = false;
		
//		while (menu){
//			if(button == Button.ID_UP) {
//				msgListener = new MessageListener(brick.getName(),true);
//				menu = false;
//			} else if(button == Button.ID_DOWN) {
//				msgListener = new MessageListener(brick.getName(),false);
//				menu = false;
//			}
//		}
		msgListener = new MessageListener(brick.getName(),true);
		BroadcastReceiver.getInstance().addListener(msgListener);
		BroadcastManager.getInstance().broadcast("{'name' : 'INIT'}".getBytes());
		Pilot.init(50,38,15);

		LEDController.switchOff();

		SensorController sControl = new SensorController();
	 	sControl.start();
	 	LEDController.switchRed();
	 	
	 	
		//Delay.msDelay(500);
	 	

		while(true){
			

			
		
			if(Pilot.distance(5) < 0.20f ){
				Pilot.stop();
		
			}else if (Pilot.distance(5) >= 0.20f){
				
				if(sControl.sample.isBlue()){
					//prevColor = Color.BLUE;
					Pilot.forward();
				}else{
					
					if(sControl.sample.isWhite()){
						//prevColor = Color.WHITE;
						Pilot.turn_right();

					}else if(sControl.sample.isBlack()){
						//prevColor = Color.BLACK;
						Pilot.turn_left();
						
					}else if(sControl.sample.isOrange()){
						Pilot.slow_forward();
						
						if( System.currentTimeMillis() - lastOrangeDetectionTime > 3000){
							
							JSONObject obj = new JSONObject();
							
							if (currentRoute) {
								LEDController.switchOrange();
							} else {
								LEDController.switchGreen();
							}
							currentRoute = !currentRoute;
							
							try {
								obj.put("name", msgListener.name);
								obj.put("isCrossing",false);
								obj.put("isWaiting", false);
								obj.put("currentRoute", msgListener.currentRoute);
								obj.put("crossRequest", true);
								
								//message sent for crossing the road
								BroadcastManager.getInstance().broadcast(obj.toString().getBytes());
								
							} catch (Exception e) {
								
							}
						}
						
						lastOrangeDetectionTime = System.currentTimeMillis();
						
					}else{
						Pilot.set_speed(50);
					}

				}
				
			}

		}
	}


}
