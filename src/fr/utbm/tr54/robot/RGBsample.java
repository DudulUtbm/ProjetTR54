package fr.utbm.tr54.robot;

/**
 * Use checkColor to update the color and then is"Color" to get a boolean 
 * @author rdulieu
 *
 */
public class RGBsample {
	
	private float red;
	private float green;
	private float blue;

	private Color color;
	
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
	
	public Color getColor(){
		return this.color;
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
			
			if(
//				this.red >= 0.20 && this.red < 0.394
//				&&
				this.green >= 0.054 && this.green < 0.085
				&&
				this.blue >= 0.012 && this.blue < 0.035){
				
					color = Color.ORANGE;
			}else{
				color=Color.WHITE;
			}
		
		}else{
			if(this.green>0.07){
				color=Color.BLUE;
			}else{
				color=Color.BLACK;
			}
		}		
	}
	//Orange :0.21-0.394 / 0.055-0.085 / 0.013-0.028
	//Blanc : 0.20-0.25 0.20-0.25 0.9-0.11
	//Noir : 0.02-0.03 0.03-0.04 0.006-0.008
	//Bleu : 0.04 0.10-0.11 0.09
	
	

	
	

	


}
