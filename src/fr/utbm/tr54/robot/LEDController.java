package fr.utbm.tr54.robot;
import lejos.hardware.Button;

/**
 * Contains method for displaying colors with the LED on the robots
 * We defined all possible colors in the attributes of this controller
 * so it's really fast to add new methods
 * @author Rora
 *
 */
class LEDController{
	
	
	public static int OFF=0;
	
	public static int GREEN=1;
	public static int RED=2;
	public static int YELLOW=3;
	
	public static int B_GREEN=4;
	public static int B_RED=5;
	public static int B_YELLOW=6;
	
	public static int FB_GREEN=7;
	public static int FB_RED=8;
	public static int FB_YELLOW=9;
	
	/**
	 * Switch the LED to red
	 */
	public static void switchRed(){
		Button.LEDPattern(LEDController.RED);
	}
	
	/**
	 * Switch the LED to orange
	 */
	public static void switchOrange(){
		Button.LEDPattern(LEDController.YELLOW);
	}
	
	/**
	 * Switch the LED to blinking orange
	 */
	public static void blinkOrange(){
		Button.LEDPattern(LEDController.B_YELLOW);
	}
	
	/**
	 * Switch the LED to green
	 */
	public static void switchGreen(){
		Button.LEDPattern(LEDController.GREEN);
	}
	
	/**
	 * Switch the LED to blinking red
	 */
	public static void blinkRed(){
		Button.LEDPattern(LEDController.B_RED);
	}
	
	/**
	 * Switch off the LED
	 */
	public static void switchOff(){
		Button.LEDPattern(LEDController.OFF);
		
	}
	
}