package fr.utbm.tr54.robot;


import java.io.IOException;
import org.json.JSONObject;

import fr.utbm.tr54.network.BroadcastManager;
import fr.utbm.tr54.network.BroadcastReceiver;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;


/**
 * Follow the track and stop before hitting obstacles (other robot)
 * @author rdulieu
 *
 */
public class MainTest {

	private static Brick brick;
	private static MessageListener msgListener;
	private static boolean waitingServer = false;

	public static void main(String[] args) throws IOException {
		
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
		
		//msgListener = new MessageListener(brick.getName(),true);
		sControl.printLCD(brick.getName(), 1, 1);
		BroadcastReceiver.getInstance().addListener(msgListener);
		Pilot.init(50,38,15);

		while(true){
			//displayStatus(); //debug method                               
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
			
//			if(msgListener.isWaiting){
//				if(msgListener.isCrossing){
//					msgListener.isWaiting = false;
//					wait = false;
//				} else {
//					//this function needs to be done : count number of wheels turn since orange mark is crossed
//					//until a reference then return false which will trigger "stop" in the next condition
//					
//				}
//			}
			
			if(msgListener.isCrossing){
				waitingServer = false;
				if(Pilot.getWheelTurn()>2500){
					msgListener.currentRoute = !msgListener.currentRoute;
					msgListener.isCrossing = false;
					msgListener.isWaiting = false;
					sendMessage(false);
					LEDController.switchOff();
				}
			}
			
			if(Pilot.distance(5) < 0.20f){
				Pilot.stop();
			}else if((msgListener.isWaiting || waitingServer)	&& Pilot.waiting()){
				Pilot.stop();
				sendMessage(true); //request again
				Delay.msDelay(1000);
		
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
						
						
						if(Pilot.getWheelTurn()>500){
							Pilot.resetWheelTurn();
							
							//msgListener.isWaiting = true;
							sendMessage(true); //first message
							waitingServer = true;
							LEDController.blinkOrange();
							
						}
						
					}

				}
				
			}
			
		}
	}
	
	public static void sendMessage(boolean crossRequest){
		JSONObject obj = new JSONObject();
		try{
			obj.put("name", msgListener.name);
			obj.put("isCrossing",msgListener.isCrossing);
			obj.put("isWaiting", msgListener.isWaiting);
			obj.put("currentRoute", msgListener.currentRoute);
			obj.put("crossRequest",crossRequest);
			BroadcastManager.getInstance().broadcast(obj.toString().getBytes());
		}catch(Exception e){
			
		}
	}
	
	public static void displayStatus(){
		LCD.clear();
		LCD.drawString(msgListener.name,1,1);
		LCD.drawString("Cross",1,2);
		LCD.drawString(Boolean.toString(msgListener.isCrossing),7,2);
		LCD.drawString("Wait",1,3);
		LCD.drawString(Boolean.toString(msgListener.isWaiting),7,3);
		LCD.drawString("Route",1,4);
		LCD.drawString(Boolean.toString(msgListener.currentRoute),7,4);
	}
	


}
