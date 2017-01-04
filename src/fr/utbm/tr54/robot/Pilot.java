package fr.utbm.tr54.robot;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;

/**
 * The pilot is the controller for the motors of the robot
 * it controls the speed, rotation and start or stop the wheels
 * @author Rora
 *
 */
public class Pilot {

	public static EV3LargeRegulatedMotor motor_left;
	public static EV3LargeRegulatedMotor motor_right;
	public static RegulatedMotor[] motors;
	public static EV3UltrasonicSensor distance_sensor;
	public static SampleProvider distance_provider;

	public static EV3ColorSensor color_sensor;
	public static SampleProvider color_provider;
	public static int MAIN_SPEED;
	public static int TURN_SPEED;
	public static float POSITION;
	public static int OPPOSITE_TURN_SPEED;


/**
 * This method initialize the robot
 * All parameters are linked. 50,38,15 is our base value. If you want to change one of them you may have to also change the others
 * @param speed (in percentage) while the robot is in a straight line 
 * @param turn_speed of the main wheel when the robot rotate (should be faster than opposite turn speed)
 * @param opposite_turn_speed, turn speed of the second wheel when the robot rotate (slower than the main wheel)
 */
public static void init(int speed, int turn_speed, int opposite_turn_speed){

		motor_left = new EV3LargeRegulatedMotor(MotorPort.B);
		motor_right = new EV3LargeRegulatedMotor(MotorPort.C);
		motors = new RegulatedMotor[] {motor_right};
		motor_left.synchronizeWith(motors);

		MAIN_SPEED = speed;
		TURN_SPEED = turn_speed;
		POSITION = 0.0f;
		OPPOSITE_TURN_SPEED = opposite_turn_speed;
		
		distance_sensor = new EV3UltrasonicSensor(SensorPort.S2);
		distance_provider = distance_sensor.getDistanceMode();
		
		set_speed(MAIN_SPEED);

	}

	/**
	 * use the ultrasonic sensor to measure a distance in front of the robot
	 * @return an array of float that contains the distance at index 0
	 */
	public static float distance(){

        float[] sample = new float[distance_provider.sampleSize()];

		distance_provider.fetchSample(sample, 0);
		return sample[0];

	}

	/**
	 * use the ultrasonic sensor to measure a distance in front of the robot
	 * @param n
	 * @return an array of float that contains the distance at index 0
	 */
	public static float distance(int n){

		MeanFilter filter = new MeanFilter(distance_provider, n);

		float[] sample = new float[filter.sampleSize()];
		distance_provider.fetchSample(sample, 0);

		return sample[0];

	}

	/**
	 *  use the ultrasonic sensor to detect the color below the robot
	 * @return an array of float (size 3) that contains the RGB detection of the color
	 */
	public static float[] color(){


		float[] sample = new float[color_provider.sampleSize()];

		color_provider.fetchSample(sample, 0);

		return sample;

	}

	/**
	 * the robot start to drive forward
	 */
	public static void forward(){

		motor_left.startSynchronization();
		motor_left.setSpeed((motor_left.getMaxSpeed()*MAIN_SPEED)/100);
		motor_right.setSpeed((motor_right.getMaxSpeed()*MAIN_SPEED)/100);
		motor_right.forward();
		motor_left.forward();
		motor_left.endSynchronization();

	}
	
	/**
	 * the robot drive forward slowly (40 % of his main speed)
	 */
	public static void slow_forward(){

		motor_left.startSynchronization();
		motor_left.setSpeed((int) ((motor_left.getMaxSpeed()*MAIN_SPEED*0.4)/100));
		motor_right.setSpeed((int) ((motor_right.getMaxSpeed()*MAIN_SPEED*0.4)/100));
		motor_right.forward();
		motor_left.forward();
		motor_left.endSynchronization();

	}

	/**
	 * the robot rotate to the right
	 */
	public static void turn_right(){

		motor_left.startSynchronization();

		motor_left.setSpeed((motor_left.getMaxSpeed()*TURN_SPEED)/100);
		motor_left.forward();
		
		motor_right.setSpeed((motor_right.getMaxSpeed()*OPPOSITE_TURN_SPEED)/100);
		motor_right.forward();

		motor_left.endSynchronization();

	}
	
	/**
	 * the robot rotate to the right slowly (65 % of his rotation speed)
	 */
	public static void slow_turn_right(){

		motor_left.startSynchronization();

		motor_left.setSpeed((motor_left.getMaxSpeed()*((float)(TURN_SPEED*0.65)))/100);
		motor_left.forward();
		
		motor_right.setSpeed((motor_right.getMaxSpeed()*OPPOSITE_TURN_SPEED)/100);
		motor_right.forward();

		motor_left.endSynchronization();

	}

	/**
	 * the robot rotate to the left
	 */
	public static void turn_left(){

		motor_left.startSynchronization();

		motor_right.setSpeed((motor_right.getMaxSpeed()*TURN_SPEED)/100);
		motor_right.forward();

		motor_left.setSpeed((motor_left.getMaxSpeed()*OPPOSITE_TURN_SPEED)/100);
		motor_left.forward();
		
		motor_left.endSynchronization();

	}

	/**
	 * the stop his movement
	 */
	public static void stop(){

		motor_left.startSynchronization();
		motor_right.stop();
		motor_left.stop();
		motor_left.endSynchronization();

	}

	/**
	 * the robot drive backward
	 */
	public static void backward(){

		motor_left.startSynchronization();
		motor_right.backward();
		motor_left.backward();
		motor_left.endSynchronization();

	}
	
	/**
	 * change the current speed of the robot. It doesn't change his main speed (forward and rotation)
	 * so it last only until the next forward, slowforward or any rotation call
	 * @param speed_percent the new speed
	 */
	public static void set_speed(float speed_percent){

		motor_left.startSynchronization();
		motor_left.setSpeed((motor_left.getMaxSpeed()*speed_percent)/100);
		motor_right.setSpeed((motor_right.getMaxSpeed()*speed_percent)/100);
		motor_left.endSynchronization();

	}

	/**
	 * rotation of a precise angle
	 * @param angle in radian
	 */
	public static void rotate(double angle){

		motor_left.startSynchronization();

		float angle_rotate = (float) (angle * (105f / 53.87f) * (180/Math.PI));

		motor_right.rotate((int)angle_rotate);
		motor_left.rotate((int)(-angle_rotate));

		motor_left.endSynchronization();

	}

	/**
	 * Reset the robot's traveled distance
	 */
	public static void resetWheelTurn() {
		motor_left.resetTachoCount();	
	}
	
	/**
	 * We use the Tachocount on the left motor to measure the distance traveled by the robot.
	 * @return the traveled distance since the last call of resetWheelTurn().
	 */
	public static int getWheelTurn(){
		return motor_left.getTachoCount();
	}
	
	/**
	 * We test a boolean that tell to the robot it has to stop his movement before the intersection
	 * the value (900) was determined by testing on the track
	 * @return true if it has to stop
	 */
	public static boolean waiting(){
		
		return motor_left.getTachoCount() >= 900;
				
		
	}

}
