package fr.utbm.tr54.robot;

import org.json.JSONObject;

import fr.utbm.tr54.network.BroadcastListener;

public class MessageListener implements BroadcastListener {
	
	String name;
	boolean currentRoute;
	boolean isCrossing;
	boolean isWaiting;
	long crossingTime;
	
	public MessageListener(String name,boolean currentRoute){
		this.name = name;
		this.currentRoute = currentRoute;
		this.isCrossing = false;
		this.isWaiting = false;
	}
	
	public void update(JSONObject obj){
		if(obj.get("currentRoute") != null)
			this.currentRoute = obj.getBoolean("currentRoute");
		if(obj.get("isCrossing")!= null)
			this.isCrossing = obj.getBoolean("isCrossing");
		if(obj.get("isWaiting")!= null)
			this.isWaiting = obj.getBoolean("isWaiting");
		if(obj.getBoolean("isCrossing")){
			crossingTime = System.currentTimeMillis();
		}
	}
	@Override
	public void onBroadcastReceived(byte[] message) {
		// TODO Auto-generated method stub
		String messageS = new String(message);
		JSONObject obj = new JSONObject (messageS);
		try{
			if(obj.get("name") != null && obj.getString("name") == this.name)
				this.update(obj);
		}catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
