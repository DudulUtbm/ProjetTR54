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
 * 
 * The main algorithm that run our robots.
 * Follow the track and stop before hitting obstacles.
 * When driving on an orange token the robot ask the server the permission to cross the intersection.
 * Depending on the answer the LED's color will change and the robot'll either stop or cross the intersection.
 * Each robot display his name on his screen.
 * @author Rora
 *
 */
public class MainTest {

	private static Brick brick;
	private static MessageListener msgListener;
	private static boolean waitingServer = false;

	/**
	 * Main function. It's explained in further details in the report.
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
		// INITIALISATION
		
		brick = BrickFinder.getLocal();
		
		SensorController sControl = new SensorController();
	 	sControl.start(); 
		
		LEDController.switchOff();
		
		sControl.printLCD("Choose your path !", 1, 1); // the user has to choose on which side of the track the robot start. 
		
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
		
		sControl.printLCD(brick.getName(), 1, 1);
		BroadcastReceiver.getInstance().addListener(msgListener);
		BroadcastManager.getInstance().broadcast("{'name' : 'INIT'}".getBytes());
		Pilot.init(50,38,15);

		//END OF INITIALISATION
		
		while(true){
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
			if(msgListener.isCrossing){
				waitingServer = false;
				if(Pilot.getWheelTurn()>2500){
					msgListener.currentRoute = !msgListener.currentRoute;
					msgListener.isCrossing = false;
					msgListener.isWaiting = false;
					sendMessage(false); //the robot signals that he is no longer crossing the intersection
					LEDController.switchOff();
				}
			}
			
			if(Pilot.distance(5) < 0.20f){
				Pilot.stop();
			}else if((msgListener.isWaiting || waitingServer)	&& Pilot.waiting()){
				Pilot.stop();
				sendMessage(true); //the robot is waiting and asking the permission to cross the intersection 
				Delay.msDelay(1000);
		
			}else if (Pilot.distance(5) >= 0.20f){
				
				if(sControl.sample.isBlue()){ //the robot drive on the blue part of the track
					Pilot.forward();
				}else{
					
					if(sControl.sample.isWhite()){ //the robot drive on the white part of the track
						if(msgListener.currentRoute == false){
							Pilot.slow_turn_right();
						}else {
							Pilot.turn_right();
						}

					}else if(sControl.sample.isBlack()){ //the robot drive on the black part of the track
						Pilot.turn_left();
						
					}else if(sControl.sample.isOrange()){ //the robot drive on the orange token
						Pilot.slow_forward();
						
						
						if(Pilot.getWheelTurn()>500){
							Pilot.resetWheelTurn();
							
							msgListener.isWaiting = true;
							sendMessage(true);  // the robot is asking the server to cross the intersection
							LEDController.blinkOrange();
							
						}
						
					}

				}
				
			}
			
		}
	}
	
	/**
	 * Send a message to the server.
	 * @param crossRequest true if the message is a crossing request. false if it's only a status update.
	 */
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
	
	/**
	 * a debug function. It display the current status of the robot on his LCD screen.
	 * It's blinking a little because of the refresh rate 
	 */
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
