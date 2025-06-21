package net.itsrelizc.health2.ballistics;

import org.bukkit.util.Vector;

public class Vectors {
	
	public static Vector getRandomUnitVector() {
	    double theta = Math.random() * 2 * Math.PI; // azimuthal angle
	    double phi = Math.acos(2 * Math.random() - 1); // polar angle
	    double x = Math.sin(phi) * Math.cos(theta);
	    double y = Math.sin(phi) * Math.sin(theta);
	    y = Math.abs(y);
	    double z = Math.cos(phi);
	    return new Vector(x, y, z).normalize();
	}
	
}
