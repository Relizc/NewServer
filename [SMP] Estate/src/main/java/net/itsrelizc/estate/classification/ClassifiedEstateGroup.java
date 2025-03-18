package net.itsrelizc.estate.classification;

import java.util.ArrayList;
import java.util.List;

public class ClassifiedEstateGroup<T extends RegisterableEstate> {
	
	private List<List<T>> dimension;
	
	public ClassifiedEstateGroup(int multi) {
		
		dimension = new ArrayList<List<T>>();
		for (int x = 0; x < multi; x ++) {
			List<T> p = new ArrayList<T>();
			for (int y = 0; y < multi; y ++) {
				p.add(null);
			}
			dimension.add(p);
		}
		
	}
	
	public void addEstate(int x, int y, T estate) {
		dimension.get(x).set(y, estate);
	}

}
