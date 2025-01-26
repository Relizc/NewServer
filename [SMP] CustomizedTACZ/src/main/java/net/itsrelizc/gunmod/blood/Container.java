package net.itsrelizc.gunmod.blood;

import org.bukkit.entity.Player;

public class Container {
	
	private double head = 3.5d;
	private double thorax = 16d;
	private double legs = 20.5d;
	private Player player;
	
	public Container(Player owner) {
		this.player = owner;
	}

}
