package com.example.computerz.cannonapp;


import android.graphics.*;
import android.view.MotionEvent;

import java.util.ArrayList;


/**
 * This is my completed homework for Part A and Part B
 *
 * This game is meant to be played in landscape
 *
 * If you press on the screen, a ball will fire
 *
 * The further away you are from the bottom left corner, the faster the ball is fired
 *
 * You may fire as many balls as you like at once
 *
 * There is gravity and wind. The wind is pushing the balls to the left
 *
 * There is a wall on the left and ground. Balls will bounce on both the wall and the ground.
 *
 * The targets move. I did not make them stop moving when they get hit because it's much more fun to hit a moving target
 * 
 * @author Nathan Tibbetts
 * @version April, 3 2017
 */
public class TestAnimator implements Animator {

	// instance variables

	// countY counts the number of logical clock ticks for each ball
	// it is kept seperate from the countX because sometimes it needs to be reset
	//countX works similarly
	//the velocity in the X and Y direction is based on the position the user presses-further away is more velocity
	private ArrayList<Integer> countY = new ArrayList<Integer>();
	private ArrayList<Integer> countX = new ArrayList<Integer>();
	private ArrayList<Double> velocityX = new ArrayList<Double>();
	private ArrayList<Double> velocityY = new ArrayList<Double>();

	private double timeY;
	private double timeX;
	private double distanceY;
	private double distanceX;

	//used to keep track of the moving targets
	private int shifterX1 = -100;
	private int shifterX2 = -100;
	private int shifterY1 = -100;
	private int shifterY2 = -100;

	//the targets start moving forwards
	private boolean forwards = true;


	//constants for ball size, acceleration from wind and gravity, target speed, and target locations
	public static final int CIRCLE_RADIUS = 10;
	public static final double ACCELERATION_Y = -.98;
	public static final double ACCELERATION_X = -.4;
	public static final int TARGET_SPEED = 5;

	public static final int TARGET_LEFT_ONE = 1000;
	public static final int TARGET_TOP_ONE = 400;
	public static final int TARGET_RIGHT_ONE = 1080;
	public static final int TARGET_BOTTOM_ONE = 480;

	public static final int TARGET_LEFT_TWO = 720;
	public static final int TARGET_TOP_TWO = 720;
	public static final int TARGET_RIGHT_TWO = 800;
	public static final int TARGET_BOTTOM_TWO = 800;






	
	/**
	 * Interval between animation frames: .03 seconds (i.e., about 33 times
	 * per second).
	 * 
	 * @return the time interval between frames, in milliseconds.
	 */
	public int interval() {
		return 30;
	}
	
	/**
	 * The background color: teal.
	 * 
	 * @return the background color onto which we will draw the image.
	 */
	public int backgroundColor() {
		// create/return the background color
		return Color.rgb(0, 139, 139);
	}
	

	/**
	 * Action to perform on clock tick
	 * 
	 * @param g the graphics object on which to draw
	 */
	public void tick(Canvas g) {

		//every tick the target gets shifted by the speed
		if(forwards) {
			shifterX1 += TARGET_SPEED;
			shifterX2 += TARGET_SPEED;
			shifterY1 += TARGET_SPEED;
			shifterY2 += TARGET_SPEED;
		}
		else{
			shifterX1 -= TARGET_SPEED;
			shifterX2 -= TARGET_SPEED;
			shifterY1 -= TARGET_SPEED;
			shifterY2 -= TARGET_SPEED;
		}

		//once it gets to a certain distance, the moving target turns around
		if((shifterX1 >= 500) || (shifterX1 <= -200)){
			forwards = !forwards;
		}



		//these targets aren't drawn, but are instead placed in the same location as the drawn rectangles
		//we will check whether the balls overlap these targets
		Rect target1 = new Rect(TARGET_LEFT_ONE + shifterX1, TARGET_TOP_ONE + shifterY1, TARGET_RIGHT_ONE + shifterX1, TARGET_BOTTOM_ONE + shifterY1);
		Rect target2 = new Rect(TARGET_LEFT_TWO + shifterX2, TARGET_TOP_TWO + shifterY2, TARGET_RIGHT_TWO + shifterX2, TARGET_BOTTOM_TWO + shifterY2);

		double velX;
		double velY;
		drawCannon(g);

		drawTargets(g, shifterX1, shifterY1, shifterX2, shifterY2);

		drawArena(g);



		Paint redPaint = new Paint(); //redpaint is more like a pink paint
		redPaint.setColor(Color.rgb(255, 20, 147));
		Paint greenPaint = new Paint();
		greenPaint.setColor(Color.GREEN);

		//goes through every ball in the array that keeps track of them
		for (int i = 0; i < countY.size(); i++) {

			//adding one to the count moves the ball every tick
			countY.set(i, countY.get(i) + 1);
			countX.set(i, countX.get(i) + 1);
			timeY = (double) countY.get(i);
			timeX = (double) countX.get(i);

			//velocity needs to be refactored or else the balls move way too fast
			velX = velocityX.get(i) / 25.0;
			velY = velocityY.get(i) / 25.0;

			//basic kinematics equation for distance. Both X and Y have accelerations for Wind and Gravity
			distanceY = timeY * velY + (.5 * ACCELERATION_Y * timeY * timeY);
			distanceX = timeX * velX + (.5 * ACCELERATION_X * timeX * timeX);

			//hitting the wall causes the ball to bounce
			if (distanceX < 0) {
				velocityX.set(i, velocityX.get(i) * .8); //the horizontal speed gets reduced by a factor of .8

				countX.set(i, 0); //the affects of wind reset at this new launch point

				timeX = (double) countX.get(i); //have to reset the timer since we reset the array

				velX = velocityX.get(i) / 25.0; //still slowing down our velocity to match the screen

				distanceX = timeX * velX + (.5 * ACCELERATION_X * timeX * timeX);
			}

			//hitting the ground causes the ball to bounce
			if (distanceY < 0) {
				velocityY.set(i, velocityY.get(i) * .8); //the vertical speed gets reduced by a factor of .8

				countY.set(i, 0); //the affects of gravity reset at this new launch point

				timeY = (double) countY.get(i); //have to reset the timer since we reset the array

				velY = velocityY.get(i) / 25.0; //still slowing down our velocity to match the screen

				distanceY = timeY * velY + (.5 * ACCELERATION_Y * timeY * timeY);


			}






			//draw the ball. distanceY has to start 1300 backwards because it counts down instead of up
			g.drawCircle((int) distanceX, (int) (1300 - distanceY), CIRCLE_RADIUS, redPaint);

			//if the ball hits the target, the ball disappears
			//is checking the logical location of the rectangles, not the actual drawn location
			if (targetAcquired(target1, target2, (int) distanceX, 1300 - (int) distanceY)) {
				g.drawRect(0, 0, 2000, 2000, greenPaint); //flashes screen green when a shot hits a target
				velocityX.remove(i); //removes the x speed from the array list
				velocityY.remove(i); //removes the y speed from the array list
				countY.remove(i); //removes the y timer from the array list
				countX.remove(i); //removes the x timer from the array list
			}
		}

	}


	/**
	 * Tells that we never pause.
	 * 
	 * @return indication of whether to pause
	 */
	public boolean doPause() {
		return false;
	}

	/**
	 * Tells that we never stop the animation.
	 * 
	 * @return indication of whether to quit.
	 */
	public boolean doQuit() {
		return false;
	}
	
	/**
	 * create a new ball when touched. This ball has velocity in relation to how far away
	 * from the bottom left corner you press
	 */
	public void onTouch(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			velocityX.add((double)event.getX());
			velocityY.add(1300.0 -(double)event.getY());
			countY.add(0);
			countX.add(0);
		}
	}

	//draws targets
	public void drawTargets(Canvas g,int x1, int y1, int x2, int y2){
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);

		Rect target1 = new Rect(TARGET_LEFT_ONE + x1, TARGET_TOP_ONE + y1, TARGET_RIGHT_ONE + x1, TARGET_BOTTOM_ONE + y1);
		Rect target2 = new Rect(TARGET_LEFT_TWO + x2, TARGET_TOP_TWO + y2, TARGET_RIGHT_TWO + x2, TARGET_BOTTOM_TWO + y2);

		g.drawRect(target1, paint);
		g.drawRect(target2, paint);
	}

	//draws borders which will be walls
	public void drawArena(Canvas g){
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		g.drawLine(0, 1300 + CIRCLE_RADIUS, 2000, 1300 + CIRCLE_RADIUS, paint);
		g.drawLine(0, 1300 + CIRCLE_RADIUS, 0, 0, paint);


	}

	//draws a simple cannon
	public void drawCannon(Canvas g){
		Paint brown = new Paint();
		brown.setColor(Color.rgb(139, 69, 19));
		g.drawRect(0, 1280, 60, 1300 + CIRCLE_RADIUS, brown);
		Paint black = new Paint();
		black.setColor(Color.BLACK);
		g.drawRect(15, 1200, 45, 1290 + CIRCLE_RADIUS, black);



	}

	//checks to see if the ball is in either target
	public boolean targetAcquired(Rect target1, Rect target2, int x, int y){


		if(target1.contains(x, y) || target2.contains(x,y)){
			return true;
		}
		else{
			return false;
		}

	}
	
	

}//class TextAnimator
