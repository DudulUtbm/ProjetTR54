package fr.utbm.tr54.robot;


import java.io.IOException;
import org.json.JSONObject;

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
	private static Brick brick;
	private static MessageListener msgListener;
	private static boolean wait;

	public static void main(String[] args) throws IOException {
		
		wait = false;
		brick = BrickFinder.getLocal();
		
		SensorController sControl = new SensorController();
	 	sControl.start();
		
		LEDController.switchOff();
		sControl.printLCD("Choose your path !", 1, 1);
		
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
		
		msgListener = new MessageListener(brick.getName(),true);
		sControl.printLCD("my name is :", 1, 1);
		sControl.printLCD(brick.getName(), 2, 2);
		BroadcastReceiver.getInstance().addListener(msgListener);
		BroadcastManager.getInstance().broadcast("{'name' : 'INIT'}".getBytes());
		Pilot.init(50,38,15);
	 	
	 	
		Delay.msDelay(500);

		while(true){
			
			//debug display
//			JSONObject objDebug = new JSONObject();
//			try{
//				objDebug.put("name", "waitingMessage");
//				objDebug.put("Wheelcount",Pilot.getWheelTurn());
//				objDebug.put("isWaiting",wait);
//				//first message sent for crossing the road
//				BroadcastManager.getInstance().broadcast(objDebug.toString().getBytes());
//			}catch(Exception e){
//				
//			}
			//end debug
			
			if(msgListener.isWaiting){
				if(msgListener.isCrossing){
					msgListener.isWaiting = false;
					wait = false;
				} else {
					//this function needs to be done : count number of wheels turn since orange mark is crossed
					//until a reference then return false which will trigger "stop" in the next condition
					wait = Pilot.waiting();
				}
			}
			if(msgListener.isCrossing){
				if(Pilot.getWheelTurn()>2500){
					msgListener.currentRoute = !msgListener.currentRoute;
					msgListener.isCrossing = false;
					msgListener.isWaiting = false;
					JSONObject obj = new JSONObject();
					try{
						obj.put("name", msgListener.name);
						obj.put("isCrossing",msgListener.isCrossing);
						obj.put("isWaiting", msgListener.isWaiting);
						obj.put("currentRoute", msgListener.currentRoute);
						obj.put("crossRequest", false);
						//first message sent for crossing the road
						BroadcastManager.getInstance().broadcast(obj.toString().getBytes());
						LEDController.switchOff();
					}catch(Exception e){
						
					}
				}
			}
			
			if(Pilot.distance(5) < 0.20f || wait ){
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
						
					}else if(sControl.sample.isOrange()){ //the robot drive on the orange token
						Pilot.slow_forward();
						Pilot.resetWheelTurn();
						
						if(Pilot.getWheelTurn()>500){
							
							JSONObject obj = new JSONObject();
							
//							if (currentRoute) {
//								LEDController.switchOrange();
//							} else {
//								LEDController.switchGreen();
//							}
							currentRoute = !currentRoute;
							
							try {
								msgListener.isCrossing = false;
								msgListener.isWaiting = false;
								obj.put("name", msgListener.name);
								obj.put("isCrossing",msgListener.isCrossing);
								obj.put("isWaiting", msgListener.isWaiting);
								obj.put("currentRoute", msgListener.currentRoute);
								obj.put("crossRequest", true);
								
								//message sent for crossing the road
								BroadcastManager.getInstance().broadcast(obj.toString().getBytes());
								LEDController.blinkRed();
								msgListener.isWaiting = false;
								
							} catch (Exception e) {
								
							}
						}
						
					}else{
						Pilot.set_speed(50);
					}

				}
				
			}
			
		}
	}


}
