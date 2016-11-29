package fr.utbm.tr54.robot;
import lejos.hardware.Button;

class LEDController{
	
	// 1/2/3: static light green/red/yellow
	// 4/5/6: normal blinking light green/red/yellow
	// 7/8/9: fast blinking light green/red/yellow
	
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
	
	
	public static void switchRed(){
		Button.LEDPattern(LEDController.RED);
	}
	
	public static void switchOrange(){
		Button.LEDPattern(LEDController.YELLOW);
	}
	
	public static void switchGreen(){
		Button.LEDPattern(LEDController.GREEN);	
	}
	
	public static void blinkRed(){
		Button.LEDPattern(LEDController.B_RED);
	}
	
	public static void switchOff(){
		
		Button.LEDPattern(LEDController.OFF);
		
	}
	
}