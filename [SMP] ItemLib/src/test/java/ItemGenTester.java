import net.itsrelizc.itemlib.RNGMath;

public class ItemGenTester {
	
	static int[] dis = new int[64];
	
	public static void main(String[] args) {
		for (int i = 0; i < 64000; i ++) {
			int x = RNGMath.getItemCount(1, 0.95, 0);
			dis[x] ++;
		}
		
		for (int i = 0; i < 64; i ++) {
			System.out.println(i + " : " + dis[i]);
		}
	}

}
