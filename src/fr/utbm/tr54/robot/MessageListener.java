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
		this.isWaiting = false;
	}
	
	public void update(JSONObject obj){
		if(obj.getString("name") != null)
			this.name = obj.getString("name");
		if(obj.get("name") != null)
			this.currentRoute = obj.getBoolean("currentRoute");
		if(obj.get("isCrossing")!= null)
			this.isCrossing = obj.getBoolean("isCrossing");
		if(obj.get("isWaiting")!= null)
			this.isWaiting = obj.getBoolean("isWaiting");
	}
	@Override
	public void onBroadcastReceived(byte[] message) {
		// TODO Auto-generated method stub
		String messageS = new String(message);
		JSONObject obj = new JSONObject (messageS);
		this.update(obj);
	}

}
