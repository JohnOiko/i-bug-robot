/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ibugproject;

import java.io.IOException;
import simbad.gui.*;
import simbad.sim.*;

/**
 *
 * @author John
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        EnvironmentDescription environment = new Env("spheres");
        //EnvironmentDescription environment = new Env("box");
        //EnvironmentDescription environment = new Env("bottle");
        //EnvironmentDescription environment = new Env("spiral");
        //EnvironmentDescription environment = new Env("cage");
        Simbad frame = new Simbad(environment, false);
    }
    
}
