package net.itsrelizc.npc;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.npc.LongDistanceNavigator.ChunkCoord;
import net.itsrelizc.npc.LongDistanceNavigator.MarkedChunkCoord;

public class CommandPath extends RelizcCommand {

	public CommandPath() {
		super("path", "debugfindingpaths");
		this.setRelizcOp(true);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onPlayerExecute(Player player, String[] args) {
		
//		double x1 = Double.valueOf(args[0]);
//		double y1 = Double.valueOf(args[1]);
//		double z1 = Double.valueOf(args[2]);
//		double x2 = Double.valueOf(args[3]);
//		double y2 = Double.valueOf(args[4]);
//		double z2 = Double.valueOf(args[5]);
//		
//		Location a = new Location(player.getLocation().getWorld(), x1, y1, z1);
//		Location b = new Location(player.getLocation().getWorld(), x2, y2, z2); 
//		
//		LongDistanceNavigator nav = new LongDistanceNavigator(a, b);
//		nav.generalizeChunks();
//		
//		player.sendMessage("Navigate from " + a + " to " + b + " (Chunkwise):");
//		for (MarkedChunkCoord c : nav.getGeneralizedChunkCoordinates()) {
//			player.sendMessage(c.toString());
//			player.spawnParticle(Particle.VILLAGER_HAPPY, c.getSelectedLocation().clone().add(0, 1,0 ), 50, 0.5, 0.5, 0.5, 0);
//		}
		
		
		return true;
	}
	
}
