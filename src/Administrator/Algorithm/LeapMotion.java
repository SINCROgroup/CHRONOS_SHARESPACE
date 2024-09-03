package Administrator.Algorithm;

import com.leapmotion.leap.*;


/**
 * @version 
 * <ul>
 * <li> 1.0: March 6, 2016 created by Maria Lombardi </li>
 * </ul>
 * <hr>
 * @author Maria Lombardi
 * <p>
 * <br>
 * The Class LeapMotion.
 * <p>
 * This class implements the interface between the Leap Motion (position 
 * sensor) and the java program. Here, several library's functions of
 * leapmotion is used to get the  values of finger's position from the sensor.
 * <br>
 */

public class LeapMotion {
	
	//private static final Logger logger = Logger.getLogger(LeapMotion.class.getName());

	Controller controller;
	
	int appWidth = 800;
	float leapX = appWidth/2;
	float previousLeapX = appWidth/2;
	
	public LeapMotion(){
		
    	// establish the connection with leap motion
    	this.controller = new Controller();
    	//enable reporting of the swipe gesture
	    this.controller.enableGesture(Gesture.Type.TYPE_SWIPE);
	}
	
	/**
	 * Gets the finger's position.
	 *
	 * @return the value of finger's position is on the x-axis. The motion is
	 * allowed only on the x-axis from left to right side and viceversa.
	 * Return a value in the range [0, 800], the same of the 
	 * screen's resolution.
	 */
	public float getLeapX(){
			 
		 InteractionBox iBox = controller.frame().interactionBox();

		 int history = 1;
		 int maxHistory = 10;
		 int[] x = new int[history];
		 int j = 0, f = 0;
		 do{
			 Frame frame = this.controller.frame(f);
			 if(frame.isValid()){
				 Hand hand = frame.hands().frontmost();
				 FingerList indexFingerList = hand.fingers().fingerType(Finger.Type.TYPE_INDEX);
			     Finger indexFinger = indexFingerList.get(0); //since there is only one per hand
			     // average on the 4 points of the finger (to be more accurate)
			     int meanBoneCount = 0;
			     for(Bone.Type boneType : Bone.Type.values()) {
			         Bone bone = indexFinger.bone(boneType);
			         //if(bone.isValid()){
			        	 x[j] += bone.center().getX();
			        	 meanBoneCount++;
			         //}
			     }
			     x[j] = x[j]/meanBoneCount;
			     j++;
			 }
			 f++;
		 }while(f<maxHistory && j<history);
	     
		 int meanX = 0;
		 for(int i = 0; i < x.length; i++){
			 meanX += x[i];
		 }
	     meanX = meanX/x.length;
		 
	     Vector normalizedPoint = iBox.normalizePoint(new Vector(meanX, 0, 0), true);
	     
	     this.leapX = normalizedPoint.getX() * this.appWidth;
	     
	     if((this.previousLeapX - this.leapX) == -this.appWidth/2){
	    	 this.leapX = 0;
	     }else if((this.previousLeapX - this.leapX) == this.appWidth/2){
	    	 this.leapX = this.appWidth;
	     }
	     
	     this.previousLeapX = this.leapX;
	     return this.leapX;
	}
}



