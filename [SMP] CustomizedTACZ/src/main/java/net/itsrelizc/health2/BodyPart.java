package net.itsrelizc.health2;

public enum BodyPart {
	
	THORAX("chest"),
	LEFT_ARM("larm"),
	RIGHT_ARM("rarm"),
	STOMACH("abs"),
	LEFT_LEG("lleg"),
	RIGHT_LEG("rleg"),
	HEAD("head");

	private String string;

	BodyPart(String string) {
		this.string = string;
	}
	
	

}
