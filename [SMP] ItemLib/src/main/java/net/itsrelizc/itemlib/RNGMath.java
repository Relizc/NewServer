package net.itsrelizc.itemlib;

public class RNGMath {
	
	public static int getItemCount(double chance, double decay, int count) {

	    while (Math.random() < chance * decay) {
	        count++;
	        chance *= decay;
	    }

	    return count;
	}


}
