package fr.utbm.tr54.robot;


import java.io.IOException;

import lejos.utility.Delay;

public class RobotComFollower {


	public static void main(String[] args) {

		Pilot.init(75, 30, 65);

		SensorController sControl = new SensorController();
	 	sControl.start();

		Receiver receiver;
		try {
			receiver = new Receiver();
			receiver.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		Delay.msDelay(500);


		while(true){

//			
//			if(receiver){
//				Pilot.stop();
//			}else if (receiver){
//				
				if(sControl.sample.isBlue()){
					Pilot.forward();
				}else{
					Pilot.set_speed(60);
					
					if(sControl.sample.isWhite()){
						Pilot.turn_right();
						while(sControl.sample.isWhite()){
							Pilot.turn_right();
						}
						
					}else if(sControl.sample.isBlack()){
						Pilot.turn_left();
						while(sControl.sample.isBlack()){
							Pilot.turn_left();
						}
						
					}else{
						Pilot.set_speed(50);
					}

				}
				
//			}

		}


	}

}
