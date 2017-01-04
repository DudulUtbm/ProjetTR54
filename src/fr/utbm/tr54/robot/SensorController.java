package fr.utbm.tr54.robot;

import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

/**
 * Contains all functions needed to use a RGB color sensor.
 * It's a runnable so it's running on a different thread than the main process.
 *  @author Rora
 *
 */
public class SensorController implements Runnable {
	private Thread sensorThread;

	EV3ColorSensor colorSensor;
	SampleProvider colorMode;
	
	public RGBsample sample;

	/**
	 * Constructor that assign port S3 to RGBsensor
	 */
	public SensorController() {
		this.colorSensor = new EV3ColorSensor(SensorPort.S3);
		colorMode = colorSensor.getRGBMode();
		colorSensor.setFloodlight(0);
	}
	
	/**
	 * get a RGB descriptor of the current color
	 * @param RGBresult will contains 3 float between 0 and 1 that represents the light intensity of Red, Green and Blue lights.
	 */
	void getRGB(float[] RGBresult) {
		colorMode.fetchSample(RGBresult,0);
	}
	
	/**
	 * a debug function that print a float on the LCD. It clear the display before printing
	 * @param item the float to be printed 
	 * @param x coordinate on the screen
	 * @param y coordinate on the screen
	 */
	public void printLCD(float item,int x,int y) {
		LCD.clear();
		LCD.drawString(Float.toString(item), x, y);
	}
	
	/**
	 * a debug function that print a string on the LCD. It clear the display before printing
	 * @param item the string to be printed 
	 * @param x coordinate on the screen
	 * @param y coordinate on the screen
	 */
	public void printLCD(String string, int x, int y) {
		LCD.clear();
		LCD.drawString(string, x, y);
	}

	/**
	 * a debug function that print a RGB color on the LCD. It clear the display before printing
	 * @param RGBresult is an array of float (size 3)
	 */
	public void printRGB(float[] RGBresult) {
		LCD.clear();
		LCD.drawString("R: "+Float.toString(RGBresult[0]), 0, 0);
		LCD.drawString("G: "+Float.toString(RGBresult[1]), 0, 1);
		LCD.drawString("B: "+Float.toString(RGBresult[2]), 0, 2);
	}

	/**
	 * the method executed by the thread. It's an infinite loop that detect the color below the robot.
	 * the main thread has to get the sample attribute to use the detected color
	 */
	@Override
	public void run() {
		for(;;){
			float[] RGBresult = {0,0,0};
			getRGB(RGBresult);
			sample = new RGBsample(RGBresult);
		}
		
	}
		
	/**
	 * start the thread
	 */
	public void start(){
		if(sensorThread==null){
			sensorThread= new Thread(this);
			sensorThread.start();
		}
	}

}
