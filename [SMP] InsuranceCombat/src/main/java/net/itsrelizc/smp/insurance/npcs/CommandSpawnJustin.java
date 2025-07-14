package net.itsrelizc.smp.insurance.npcs;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.string.StringUtils;

public class CommandSpawnJustin extends RelizcCommand {

	public CommandSpawnJustin() {
		super("spawnjustin", "spawnsjustin");
		this.setRelizcOp(true);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onPlayerExecute(Player player, String[] args) {
		NPC tar = null;
		for (NPC npc : CitizensAPI.getNPCRegistry()) {
		    String name = npc.getName();
		    if (name.startsWith("=body")) {
		    	tar = npc;
		    }
		}
		
		NPCJustin justin = new NPCJustin(tar);
		justin.npc.setProtected(true);
		justin.spawn();
		
		justin.moveToBody();
		//justin.despawn();
		
		//player.sendMessage("Justin spawned at " + justin.npc.getEntity().getLocation());
		
		//player.teleport(justin.npc.getEntity().getLocation());
		
		return true;
	}
	
	@Override
	public TabCompleteInfo onTabComplete(CommandSender sender, String alias, String[] args, Location location) {
		//return new TabCompleteInfo(new TabCompleteType[] {}, (Player) sender, "commands.general.tabcomplete.test");
		Entity ent = getTargetedEntity((Player) sender, 50);
		String as = "no_entity";
		if (ent != null) as=ent.getUniqueId().toString();
		return TabCompleteInfo.presetOption((Player) sender, "Body UUID", StringUtils.fromArgs(as));
		
	}
	
	// Helper function to get targeted entity
	private Entity getTargetedEntity(Player player, double maxDistance) {
	    Location eye = player.getEyeLocation();
	    Vector direction = eye.getDirection().normalize();

	    List<Entity> nearbyEntities = player.getNearbyEntities(maxDistance, maxDistance, maxDistance);
	    Entity closest = null;
	    double closestDistance = maxDistance;

	    for (Entity entity : nearbyEntities) {
	        // Skip self
	        if (entity == player) continue;

	        BoundingBox box = entity.getBoundingBox().expand(0.3); // Expand a bit to make detection easier

	        for (double i = 0; i < maxDistance; i += 0.3) {
	            Vector point = eye.toVector().add(direction.clone().multiply(i));
	            if (box.contains(point)) {
	                double distance = eye.distance(point.toLocation(player.getWorld()));
	                if (distance < closestDistance) {
	                    closest = entity;
	                    closestDistance = distance;
	                }
	                break;
	            }
	        }
	    }

	    return closest;
	}
}
