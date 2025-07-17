package net.itsrelizc.commands;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;




public class DebugAnticheatModBlocks extends RelizcCommand {
	
	public DebugAnticheatModBlocks() {
		super("debuganticheatmodblocks", "stub");
		// TODO Auto-generated constructor stub
	}
	
	private static String getNamespacedId(Material mat) {
		//Bukkit.broadcastMessage(block.getType().getKey() + " " + block.getType().get)
		
		ItemStack bukkitItem = new ItemStack(mat); // any Bukkit ItemStack
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(bukkitItem);
		Item item = nmsItem.getItem();

		// Get ID like "minecraft:stone" or "modid:custom_item"
		ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
		String fullId = id.toString();
		
		return fullId;
	}
	
	private static Queue<Material> checker = new ArrayDeque<Material>();
	
	@Override
	public boolean onPlayerExecute(Player player, String[] args) {
		
		checker.clear();
		content.clear();
		
		Location core = player.getLocation().clone().add(0, 5, 0);
		
		for (int x = core.getBlockX() - 2; x <= core.getBlockX() + 2; x ++) {
			for (int z = core.getBlockZ() - 2; z <= core.getBlockZ() + 2; z ++) {
				new Location(core.getWorld(), x, core.getBlockY(), z).getBlock().setType(Material.BEDROCK);
			}
		}
		
		Bukkit.broadcastMessage("platform spawned");
		
		long a = 0; long b= 0;
		
		for (Material mat : Material.values()) {
			String nam = getNamespacedId(mat);
			if (nam.startsWith("minecraft")) {
				a ++;
				continue;
			}
			
			checker.add(mat);
			
			b ++;
		}
		
		place(core.clone().add(0, 2, 0), checker.poll());	
		
		Bukkit.broadcastMessage("Will proceed to check " + b + " out of " + a + " total material registeries");
		
		return true;
		
	}
	
	private static List<String> content = new ArrayList<String>();
	
	public static void place(Location loc, Material block) {
		
		if (block == null) {
			Bukkit.broadcastMessage("Found " + content.size() + " possible types");
			return;
		}
		
		
		loc.getBlock().setType(block);
		if (loc.getBlock().getType() == null || loc.getBlock().getType() == Material.AIR) {
			Bukkit.broadcastMessage(block + ": skipped due to being air");
			place(loc, checker.poll());	
			return;
		} else {
			content.add(getNamespacedId(block));
			place(loc, checker.poll());	
			return;
		}
		
//		Location snowball = loc.clone().add(0, 1.5, 0);
//		
//		for (double x = snowball.getBlockX(); x <= snowball.getBlockX() + 1; x += 0.25) {
//			for (double z = snowball.getBlockZ(); z <= snowball.getBlockZ() + 1; z += 0.25) {
//				
//			}
//		}
		
	}

}
