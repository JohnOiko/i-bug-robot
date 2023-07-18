/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ibugproject;

import simbad.sim.*;
import static ibugproject.SimpleBehaviors.*;

/**
 *
 * @author John
 */
public class IBug {
    static double MAX_AVG_LIGHT_GOAL = 0.2878;
    static double MIN_AVG_LIGHT_GOAL = 0.25;
    static double HIGH_LUM_DIF = 0.025;
    static double CIRCLE_DETECTION_LUM_DIF = 0.1;
    static double SAFETY=0.7;
    static boolean DEBUG = false;
    
    private robotState state;
    public enum robotState {
        Orient, MoveForward, CircumNavigate, Stop
    }
    
    Agent rob;
    RangeSensorBelt sonars;
    LightSensor centerLight;
    LightSensor leftLight;
    LightSensor rightLight;
    double spLum = -1;
    double highLum = 0;
    boolean clockwise;
    boolean circle = false;
    boolean hitGoal = false;
    boolean stopped = false;
    
    public IBug(Agent rob, RangeSensorBelt sonars, LightSensor centerLight, LightSensor leftLight, LightSensor rightLight, boolean CLOCKWISE){
        this.rob=rob;
        this.sonars=sonars;
        this.centerLight = centerLight;
        this.leftLight = leftLight;
        this.rightLight = rightLight;
        this.clockwise=CLOCKWISE;
        state=robotState.Orient;
    }
    
    public void step() {
        double minDist;
        switch (state) {
            case Orient:
                if (Double.isNaN(Math.pow(centerLight.getLux(), 0.1))) {
                    stop(rob);
                    state = robotState.Stop;
                }
                
                else if ((leftLight.getAverageLuminance() != 0 || rightLight.getAverageLuminance() != 0) && Math.abs(leftLight.getAverageLuminance() - rightLight.getAverageLuminance()) <= LEFT_RIGHT_LUM_DIF_THRES) {
                    stop(rob);
                    state = robotState.MoveForward;
                    if (DEBUG) System.out.println("Orienting done");
                }
                
                else {
                    orient(rob, centerLight, leftLight, rightLight);
                    if (DEBUG) System.out.println("Orienting continued");
                }
                
                break;
            
            case MoveForward:
                minDist = sonars.getMeasurement(0);
                for (int i=1; i < sonars.getNumSensors(); i++)
                {
                    if (sonars.getMeasurement(i) < minDist && ((i >= 0 && i <= 2) || (i >= 10 && i <= 11)))
                        minDist = sonars.getMeasurement(i);
                }
                double avgSideLum = (leftLight.getAverageLuminance() + rightLight.getAverageLuminance()) / 2;
                
                if (Math.abs(leftLight.getAverageLuminance() - rightLight.getAverageLuminance()) > LEFT_RIGHT_LUM_DIF_THRES) {
                    stop(rob);
                    state = robotState.Orient;
                    if (DEBUG) System.out.println("Forward movement stopped because of deviation from orientation");
                }
                
                else if (avgSideLum < MAX_AVG_LIGHT_GOAL && avgSideLum > MIN_AVG_LIGHT_GOAL && minDist == sonars.getMaxRange()) {
                    hitGoal = true;
                    stop(rob);
                    state = robotState.Stop;
                    if (DEBUG) System.out.println("Forward movement stopped because goal was hit");
                    break;
                }
                
                else if (minDist < SAFETY) {
                    spLum = -1;
                    circle = false;
                    highLum = Math.pow(centerLight.getLux(), 0.1);
                    stop(rob);
                    state = robotState.CircumNavigate;
                    if (DEBUG) System.out.println("Forward movement stopped because of object");
                }
                
                else {
                    moveForward(rob, sonars, centerLight, leftLight, rightLight);
                    if (DEBUG) System.out.println("Forward movement continued");
                }
                
                break;
            
            case CircumNavigate:
                if (Math.pow(centerLight.getLux(), 0.1) - highLum > HIGH_LUM_DIF) {
                    stop(rob);
                    state = robotState.Orient;
                    if (DEBUG) System.out.println("Circum navigation stopped because of local max");
                }
                
                else if (Math.abs(Math.pow(centerLight.getLux(), 0.1) - spLum) < 0.0002 && circle) {
                    stop(rob);
                    state = robotState.Stop;
                    if (DEBUG) System.out.println("Circum navigation stopped because of circle");
                }
                
                else {
                    if (spLum == -1) {
                        spLum = Math.pow(centerLight.getLux(), 0.1);
                        if (DEBUG) System.out.println("Circum navigation started");
                    }
                    
                    else if (Math.abs(Math.pow(centerLight.getLux(), 0.1) - spLum) > CIRCLE_DETECTION_LUM_DIF && !circle) {
                        circle=true;
                        if (DEBUG) System.out.println("Circum navigation continued with circle detection enabled");
                    }
                    
                    else if (DEBUG) System.out.println("Circum navigation continued");
                    circumNavigate(rob, sonars, clockwise);
                } 
                
                break;
                
            case Stop:
                stopped = true;
                stop(rob);
                if (hitGoal) System.out.println("Reached the goal");
                else if (Double.isNaN(Math.pow(centerLight.getLux(), 0.1))) System.out.println("Can't detect light");
                else System.out.println("Circle detected");
                break;
        }
    }
    
    public boolean stopped() {
        return stopped;
    }
}
