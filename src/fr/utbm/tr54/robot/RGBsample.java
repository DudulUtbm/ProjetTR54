package fr.utbm.tr54.robot;

/**
 *  this sample contains the RGB descriptor of a color.
 *  it can contain any color but will determine the precise color only for 4 of them :
 *  White, Black, Blue or Orange. They are the colors that we use on the track
 * @author Rora
 *
 */
public class RGBsample {
	
	private float red;
	private float green;
	private float blue;

	private Color color;
	
	/**
	 * public constructor that build the sample from an array (size 3)
	 * @param sample is a 3 float tab
	 */
	public RGBsample(float[] sample){
		this.red=sample[0];
		this.green=sample[1];
		this.blue=sample[2];
		this.checkColor();
	}
	
	/**
	 * public getter of the color
	 * @return a Color from the enumeration
	 */
	public Color getColor(){
		return this.color;
	}
	
	/**
	 * check if the color is white
	 * @return true if it's white
	 */
	public boolean isWhite(){	
		
		return color==Color.WHITE;
	}
	
	/**
	 * check if the color is black
	 * @return true if it's black
	 */
	public boolean isBlack(){
		
		return color==Color.BLACK;
	}
	
	/**
	 * check if the color is blue
	 * @return true if it's blue
	 */
	public  boolean isBlue(){
		
		return color==Color.BLUE;
	}

	/**
	 * check if the color is orange
	 * @return true if it's orange
	 */
	public boolean isOrange(){
		
		return color==Color.ORANGE;
	}
	
	/**
	 * Use checkColor to update the color and then is"Color" to get a boolean
	 */
	void checkColor(){
		
		if(this.red>0.10){
			
			if(
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
	
	

	
	

	


}
