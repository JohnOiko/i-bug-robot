/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ibugproject;

import javax.vecmath.Point3d;
import simbad.sim.*;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

/**
 *
 * @author John
 */
public class Env extends EnvironmentDescription {
    public static double lightHeight = 2;
    public static Point3d defaultGoal = new Point3d(0, 0, 0);
    public static Point3d boxGoal = new Point3d(0, 0, -9);
    public static Point3d spheresGoal = new Point3d(0, 0, -9);
    public static Point3d bottleGoal = new Point3d(0, 0, -9);
    public static Point3d spiralGoal = new Point3d(-1, 0, -3);
    public static Point3d cageGoal = new Point3d(0, 0, -9);
    public static Vector3d startPos = new Vector3d(0, 0, 8);
    
    public Env(String envType){
        Point3d goal = defaultGoal;
        switch (envType.trim().toLowerCase()) {
            case "spheres":
                spheresEnv();
                goal = spheresGoal;
                break;
                
            case "box":
                boxEnv();
                goal = boxGoal;
                break;
                
            case "bottle":
                bottleEnv();
                goal = bottleGoal;
                break;
                
            case "spiral":
                spiralEnv();
                goal = spiralGoal;
                break;
                
            case "cage":
                cageEnv();
                goal = cageGoal;
                startPos = new Vector3d(0, 0, 0);
                break;
        }
        
        ambientLightColor = white;
        backgroundColor = ligthgray;
        archColor = red;
        boxColor = darkgray;
        wallColor = blue;
        floorColor = white;
        
        light1SetPosition(goal.x, 2, goal.z);
        light1IsOn = true;
        this.light2SetPosition(goal.x, 2, goal.z);
        light2IsOn=false;
        
        add(new MyRobot(startPos, "my robot",  goal));
        add(new CherryAgent(new Vector3d(goal), "goal", 0.2f));
    }
    
    private void spheresEnv() {
        add(new Box(new Vector3d(0, 0, 3), new Vector3f(5, 1, 5), this));
        add(new Box(new Vector3d(2.75, 0, 3), new Vector3f(0.5f, 1, 4), this));
        add(new Box(new Vector3d(3.25, 0, 3), new Vector3f(0.5f, 1, 3), this));
        add(new Box(new Vector3d(-2.75, 0, 3), new Vector3f(0.5f, 1, 4), this));
        add(new Box(new Vector3d(-3.25, 0, 3), new Vector3f(0.5f, 1, 3), this));
        add(new Box(new Vector3d(0, 0, -4), new Vector3f(2, 1, 2), this));
    }
    
    private void boxEnv() {
        add(new Box(new Vector3d(0, 0, 0), new Vector3f(5, 1, 5), this));
    }
    
    private void bottleEnv() {
        add(new Box(new Vector3d(0, 0, 3), new Vector3f(5, 1, 5), this));
        add(new Box(new Vector3d(0, 0, -0.5), new Vector3f(2, 1, 2), this));
    }
    
    private void spiralEnv() {
        add(new Box(new Vector3d(0, 0, 7), new Vector3f(14, 1, 1), this));
        add(new Box(new Vector3d(-2, 0, 3), new Vector3f(12, 1, 1), this));
        add(new Box(new Vector3d(0, 0, -9), new Vector3f(16, 1, 1), this));
        add(new Box(new Vector3d(-7.5, 0, -2.5), new Vector3f(1, 1, 12), this));
        add(new Box(new Vector3d(7.5, 0, -0.5), new Vector3f(1, 1, 16), this));
        add(new Box(new Vector3d(3.5, 0, -1), new Vector3f(1, 1, 7), this));
        add(new Box(new Vector3d(0, 0, -5), new Vector3f(7, 1, 1), this));
        add(new Box(new Vector3d(-3.5, 0, -2.5), new Vector3f(1, 1, 4), this));
        add(new Box(new Vector3d(-1.5, 0, -1), new Vector3f(3, 1, 1), this));
    }
    
    private void cageEnv() {
        add(new Box(new Vector3d(0, 0, -5), new Vector3f(12, 1, 1), this));
        add(new Box(new Vector3d(-6, 0, 0.5), new Vector3f(1, 1, 12), this));
        add(new Box(new Vector3d(6, 0, 0.5), new Vector3f(1, 1, 12), this));
        add(new Box(new Vector3d(0, 0, 6), new Vector3f(12, 1, 1), this));
    }
}
