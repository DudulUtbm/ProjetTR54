package fr.utbm.tr54;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;

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
		

		//distance_sensor = new EV3UltrasonicSensor(SensorPort.S3);
		//distance_provider = distance_sensor.getDistanceMode();

		//color_sensor = new EV3ColorSensor(SensorPort.S2);
		//color_provider = color_sensor.getRGBMode();
		//color_sensor.setFloodlight(0);
		set_speed(MAIN_SPEED);

	}

	public static float distance(){

        float[] sample = new float[distance_provider.sampleSize()];

		distance_provider.fetchSample(sample, 0);

		return sample[0];

	}

	public static float distance(int n){

		MeanFilter filter = new MeanFilter(distance_provider, n);

		float[] sample = new float[filter.sampleSize()];
		distance_provider.fetchSample(sample, 0);

		return sample[0];

	}

	public static float[] color(){


		float[] sample = new float[color_provider.sampleSize()];

		color_provider.fetchSample(sample, 0);

		return sample;

	}





	public static void forward(){

		motor_left.startSynchronization();
		motor_left.setSpeed((motor_left.getMaxSpeed()*MAIN_SPEED)/100);
		motor_right.setSpeed((motor_right.getMaxSpeed()*MAIN_SPEED)/100);
		motor_right.forward();
		motor_left.forward();
		motor_left.endSynchronization();

	}

	public static void turn_left(){

		motor_left.startSynchronization();

		motor_left.setSpeed((motor_left.getMaxSpeed()*TURN_SPEED)/100);
		motor_left.forward();
		
		motor_left.setSpeed((motor_left.getMaxSpeed()*OPPOSITE_TURN_SPEED)/100);
		motor_right.forward();
		//motor_right.setSpeed((motor_right.getMaxSpeed()*TURN_SPEED)/100);

		//motor_right.forward();
		//motor_left.stop();



		motor_left.endSynchronization();

	}

	public static void turn_right(){

		motor_left.startSynchronization();

		//motor_left.setSpeed((motor_left.getMaxSpeed()*TURN_SPEED)/100);
		motor_right.setSpeed((motor_right.getMaxSpeed()*TURN_SPEED)/100);
		motor_right.forward();

		//motor_right.stop();
		motor_right.setSpeed((motor_right.getMaxSpeed()*OPPOSITE_TURN_SPEED)/100);
		motor_left.forward();
		
		motor_left.endSynchronization();

	}

	public static void stop(){

		motor_left.startSynchronization();
		motor_right.stop();
		motor_left.stop();
		motor_left.endSynchronization();

	}

	public static void backward(){

		motor_left.startSynchronization();
		motor_right.backward();
		motor_left.backward();
		motor_left.endSynchronization();


	}

	public static void set_speed(float speed_percent){

		motor_left.startSynchronization();
		motor_left.setSpeed((motor_left.getMaxSpeed()*speed_percent)/100);
		motor_right.setSpeed((motor_right.getMaxSpeed()*speed_percent)/100);
		motor_left.endSynchronization();

	}

	public static void rotate(double angle){

		motor_left.startSynchronization();

		float angle_rotate = (float) (angle * (105f / 53.87f) * (180/Math.PI));

		motor_right.rotate((int)angle_rotate);
		motor_left.rotate((int)(-angle_rotate));

		motor_left.endSynchronization();

	}
	



}
