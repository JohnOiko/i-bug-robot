/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ibugproject;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import simbad.sim.Agent;
import simbad.sim.LightSensor;
import simbad.sim.RangeSensorBelt;

/**
 *
 * @author John
 */
public class SimpleBehaviors {
    static double K1 = 5;
    static double K2 = 0.7;
    static double K3 = 1;  
    static double SAFETY = 0.8;
    static double MAX_ROT_VEL = 5;
    static double MAX_TRANS_VEL = 1;
    static double LEFT_RIGHT_LUM_DIF_THRES = 0.01;
    
    public static void stop(Agent rob) {
        rob.setRotationalVelocity(0);
        rob.setTranslationalVelocity(0);
    }
    
    public static void moveForward(Agent rob, RangeSensorBelt sonars, LightSensor centerLight, LightSensor leftLight, LightSensor rightLight) {
        rob.setRotationalVelocity(0);
        double minDist = sonars.getMeasurement(0);
        for (int i = 1; i < sonars.getNumSensors(); i++) {
            if (sonars.getMeasurement(i) < minDist && ((i >= 0 && i <= 3) || (i >= 9 && i <= 11)))
                minDist = sonars.getMeasurement(i);
        }
            
        if (minDist < sonars.getMaxRange()) {
            rob.setTranslationalVelocity((minDist / sonars.getMaxRange()) * MAX_TRANS_VEL);
        }
        else {
            rob.setTranslationalVelocity ((((leftLight.getAverageLuminance() + rightLight.getAverageLuminance()) / 2) / Math.pow(centerLight.getLux(),0.1)) * MAX_TRANS_VEL);
        }        
    }
    
    public static void orient(Agent rob, LightSensor centerLight, LightSensor leftLight, LightSensor rightLight){
        rob.setTranslationalVelocity (0);
        if ((leftLight.getAverageLuminance() + rightLight.getAverageLuminance())/2 == 0) {
            rob.setRotationalVelocity(0.5 * MAX_ROT_VEL);
        }
        else if (Math.abs(leftLight.getAverageLuminance() - rightLight.getAverageLuminance()) <= LEFT_RIGHT_LUM_DIF_THRES) {
            rob.setRotationalVelocity(0);
        }
        else {
            rob.setRotationalVelocity((leftLight.getAverageLuminance() - rightLight.getAverageLuminance()) * MAX_ROT_VEL);
        }
    }
    
    public static void circumNavigate(Agent rob, RangeSensorBelt sonars, boolean CLOCKWISE){
        int min = 0;
        for (int i = 1; i < sonars.getNumSensors(); i++)
            if (sonars.getMeasurement(i) < sonars.getMeasurement(min))
                min = i;
        Point3d p = Tools.getSensedPoint(rob, sonars, min);
        double d = p.distance(new Point3d(0, 0, 0));  
        Vector3d v;
        v = CLOCKWISE? new Vector3d(-p.z, 0, p.x): new Vector3d(p.z, 0, -p.x);
        double phLin = Math.atan2(v.z, v.x);
        double phRot = Math.atan(K3 *(d - SAFETY));
        if (CLOCKWISE)
            phRot = -phRot;
        double phRef = Tools.wrapToPi(phLin + phRot); 
        
        rob.setRotationalVelocity(K1 * phRef);
        rob.setTranslationalVelocity(K2 * Math.cos(phRef));       
    }
}
