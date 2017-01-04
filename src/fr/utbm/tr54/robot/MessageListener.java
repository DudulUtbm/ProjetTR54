package fr.utbm.tr54.robot;

import org.json.JSONObject;

import fr.utbm.tr54.network.BroadcastListener;

/**
 * We use this class to communicate with the server through JON objects
 * @author Rora
 *
 */
public class MessageListener implements BroadcastListener {
	
	String name;
	boolean currentRoute;
	boolean isCrossing;
	boolean isWaiting;
	
	/**
	 * public constructor
	 * @param name of the robot
	 * @param currentRoute of the robot
	 */
	public MessageListener(String name,boolean currentRoute){
		this.name = name;
		this.currentRoute = currentRoute;
		this.isCrossing = false;
		this.isWaiting = false;
	}
	
	/**
	 * update the status of the robot following a JSON object received from the server
	 * @param obj given by the server
	 */
	public void update(JSONObject obj){
		
		if(obj.has("currentRoute"))
			this.currentRoute = obj.getBoolean("currentRoute");
		if(obj.has("isCrossing"))
			this.isCrossing = obj.getBoolean("isCrossing");
		if(obj.has("isWaiting")){
			this.isWaiting = obj.getBoolean("isWaiting");
			if(this.isWaiting)LEDController.blinkRed();
		}
			
		switch(obj.getInt("position")){
		case 1:
			LEDController.switchGreen();
			break;
		case 2:
			LEDController.switchOrange();
			break;
		default:
			LEDController.switchRed();
		}
	}
	
	/**
	 * Convert the received byte message in a JSON object.
	 * This method ignore messages send by the robot itself to avoid conflicts.
	 */
	@Override
	public void onBroadcastReceived(byte[] message) {
		String messageS = new String(message);
		JSONObject obj = new JSONObject (messageS);
		
		try{
			if(obj.getString("name").equals(this.name) && obj.has("sender")){
				this.update(obj);
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}

}
