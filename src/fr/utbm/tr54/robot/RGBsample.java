package fr.utbm.tr54.robot;

/**
 * Use checkColor to update the color and then is"Color" to get a boolean 
 * @author rdulieu
 *
 */
public class RGBsample {
	
	float red;
	float green;
	float blue;

	Color color;
	
	/**
	 * 
	 * @param sample is a 3 float tab
	 */
	RGBsample(float[] sample){
		this.red=sample[0];
		this.green=sample[1];
		this.blue=sample[2];
		this.checkColor();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isWhite(){	
		
		return color==Color.WHITE;
	}
	
	public boolean isBlack(){
		
		return color==Color.BLACK;
	}
	

	public  boolean isBlue(){
		
		return color==Color.BLUE;
	}

	public boolean isOrange(){
		
		return color==Color.ORANGE;
	}
	
//	double inRange(RGBsample sample){
//		
//		
//		
//		return 
//	}
	
	void checkColor(){
		if(this.red>0.10){
			if(this.green>0.15){
				color=Color.WHITE;
			}else{
				color=Color.ORANGE;
			}
		}else{
			if(this.green>0.07){
				color=Color.BLUE;
			}else{
				color=Color.BLACK;
			}
		}		
	}
	
	//Blanc : 0.20-0.25 0.20-0.25 0.9-0.11
	//Noir : 0.02-0.03 0.03-0.04 0.006-0.008
	//Bleu : 0.04 0.10-0.11 0.09
	//Orange : 0.15-0.25 0.05 0.01
	


}
