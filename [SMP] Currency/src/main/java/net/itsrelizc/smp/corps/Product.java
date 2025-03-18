package net.itsrelizc.smp.corps;

import net.itsrelizc.smp.corps.api.Business;

public class Product {
	
	private Business offer;
	private Business customer;
	private String id;
	private String name;
	private double cost;

	public Product(Business offer, Business customer, String id, String name, double cost) {
		
		this.offer = offer;
		this.customer = customer;
		this.id = id;
		this.name = name;
		this.cost = cost;
		
	}

}
