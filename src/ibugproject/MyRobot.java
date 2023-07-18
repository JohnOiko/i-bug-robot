/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ibugproject;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import simbad.sim.*;

/**
 *
 * @author John
 */
public class MyRobot extends Agent {
    RangeSensorBelt sonars;
    LightSensor centerLight;
    LightSensor leftLight;
    LightSensor rightLight;
    IBug iBug;

    public MyRobot (Vector3d position, String name, Point3d goal) 
    {
        super(position,name);
        sonars = RobotFactory.addSonarBeltSensor(this, 12);
        centerLight = RobotFactory.addLightSensor(this, new Vector3d(0, 0.47, 0), 0, "center");
        leftLight = RobotFactory.addLightSensorLeft(this);
        rightLight = RobotFactory.addLightSensorRight(this);
        centerLight.setUpdateOnEachFrame(true);
        leftLight.setUpdateOnEachFrame(true);
        rightLight.setUpdateOnEachFrame(true);
        iBug = new IBug(this, sonars, centerLight, leftLight, rightLight, false);

    }
    public void initBehavior() {}
    
    public void performBehavior(){
        if (!iBug.stopped()) {
            iBug.step();
        }
    }
}
