package fr.utbm.tr54.robot;

import org.json.JSONObject;

import fr.utbm.tr54.network.BroadcastListener;

public class MessageListener implements BroadcastListener {
	
	String name;
	boolean currentRoute;
	boolean isCrossing;
	boolean isWaiting;
	
	public MessageListener(String name,boolean currentRoute){
		this.name = name;
		this.currentRoute = currentRoute;
		this.isCrossing = false;
		this.isWaiting = false; //useless ?
	}
	
	public void update(JSONObject obj){
		
		if(obj.has("currentRoute"))
			this.currentRoute = obj.getBoolean("currentRoute");
		if(obj.has("isCrossing"))
			this.isCrossing = obj.getBoolean("isCrossing");
		if(obj.has("isWaiting")){
			this.isWaiting = obj.getBoolean("isWaiting");
			if(this.isWaiting)LEDController.blinkOrange();
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
	@Override
	public void onBroadcastReceived(byte[] message) {
		// TODO Auto-generated method stub
		String messageS = new String(message);
		JSONObject obj = new JSONObject (messageS);
		
		try{
			if(obj.getString("name").equals(this.name) && obj.has("sender")){
				this.update(obj);
			}
		}catch(Exception e){
			//TODO handle
		}

	}

}
