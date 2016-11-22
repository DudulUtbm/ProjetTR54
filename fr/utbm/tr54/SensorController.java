package fr.utbm.tr54;

import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

/**
 * Contains all functions needed to use a RGB color sensor.
 * We need to create an object and then call the function start for this object.
 *  @author rdulieu
 *
 */
public class SensorController implements Runnable {
	private Thread sensorThread;

	EV3ColorSensor colorSensor;
	SampleProvider colorMode;
	
	public RGBsample sample;
	
	public float currentPosition;

	/**
	 * Constructor that assign port S2 to RGBsensor
	 */
	public SensorController() {
		this.colorSensor = new EV3ColorSensor(SensorPort.S3);
		colorMode = colorSensor.getRGBMode();
		colorSensor.setFloodlight(0);
	}
	
	/**
	 * 
	 * @param RGBresult will contains 3 float between 0 and 1 that represents the light intensity of Red, Green and Blue lights.
	 */
	void getRGB(float[] RGBresult) {
		colorMode.fetchSample(RGBresult,0);
	}
	
	/**
	 * 
	 * @param item
	 */
	void printLCD(float item,int x,int y) {
		LCD.clear();
		LCD.drawString(Float.toString(item), x, y);
	}


	public void printRGB(float[] RGBresult) {
		LCD.clear();
		LCD.drawString("R: "+Float.toString(RGBresult[0]), 0, 0);
		LCD.drawString("G: "+Float.toString(RGBresult[1]), 0, 1);
		LCD.drawString("B: "+Float.toString(RGBresult[2]), 0, 2);
	}

	@Override
	public void run() {
		for(;;){
			float[] RGBresult = {0,0,0};
			getRGB(RGBresult);
			sample = new RGBsample(RGBresult);
			//send "sample" to the other thread for using
//			printRGB(RGBresult);
//			LCD.drawString(Boolean.toString(sample.isWhite()), 0, 4);
//			
//			Delay.msDelay(2000);
		}
		
	}
	
	public void updatePosition(){
		if(sample.isOrange()){
			currentPosition = 0;
		}
		
		
	}
	
	public float getCurrentPosition(){
		return currentPosition;
	}
	public void start(){
		if(sensorThread==null){
			sensorThread= new Thread(this);
			sensorThread.start();
		}
	}

}
