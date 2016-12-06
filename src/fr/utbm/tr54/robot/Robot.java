package fr.utbm.tr54.robot;

public class Robot {
	String name;
	int currentRoute;
	boolean isCrossing;
	boolean isWaiting;
	
	public Robot(){
		this.name = name;
		this.currentRoute = currentRoute;
		this.isCrossing = false;
		this.isWaiting = false;
	}
	
	public void update(JSONObject obj){
		this.name = obj.getString("name");
		this.currentRoute = obj.getBoolean("isWaiting");
		this.isCrossing = obj.getBoolean("isCrossing");
		this.isWaiting = obj.getBoolean("isWaiting");
	}

}
