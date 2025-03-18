package net.itsrelizc.smp.locker;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.plugin.java.JavaPlugin;

import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.menus.ItemGenerator;
import net.itsrelizc.nbt.BlockMetadata;
import net.itsrelizc.nbt.NBT;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;
import net.minecraft.nbt.NBTTagCompound;

public class Main extends JavaPlugin implements Listener {
	
	List<Material> interactive = new ArrayList<Material>();
	
	Material mat = Material.getMaterial("RELIZC_KEYCARD");
	
	@Override
	public void onEnable() {
		interactive.add(Material.getMaterial("TACZ_GUN_SMITH_TABLE"));
		interactive.add(Material.CHEST);
		
		EventRegistery.register(this);
	}
	
	@EventHandler
	public void rightClick(PlayerInteractEvent event) {
		
		if (event.getHand() == EquipmentSlot.HAND) {
			
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				
				ItemStack i = event.getItem();
				if (i.getType() == mat) {
					
					ItemMeta im = i.getItemMeta();
					NamespacedKey key = new NamespacedKey(EventRegistery.main, "locked");
					if (im.getCustomTagContainer().hasCustomTag(key, ItemTagType.STRING)) {
						
						event.getPlayer().sendMessage(Locale.get(event.getPlayer(), "item.keycard.interact_locked"));
						
						event.setCancelled(true);
						return;
					}
					
					UUID gen = UUID.randomUUID();
					
					i.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
					
					
					List<String> k = StringUtils.fromArgs(Locale.get(event.getPlayer(), "item.keycard.locked"));
					im.setLore(k);
					
					
					im.getCustomTagContainer().setCustomTag(key, ItemTagType.STRING, gen.toString());
					
					i.setItemMeta(im);
					
					System.out.println(event.getClickedBlock());
					
					BlockMetadata.addData(event.getClickedBlock(), "locked", gen.toString());
					
					
					
				}
				
				
			}
			
		}
		
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
	    Block block = event.getBlock();
	    Player player = event.getPlayer();
	    
	    if (interactive.contains(block.getType())) event.setCancelled(true);
	    
	    
	}
	
	@EventHandler
	public void onExplosion(EntityExplodeEvent event) {
	    List<Block> affectedBlocks = event.blockList();
	    for (Block block : affectedBlocks) {
	        // Handle block broken by explosion (TNT, creepers, etc.)
	    	if (interactive.contains(block.getType())) event.setCancelled(true);
	    }
	}
	
	@EventHandler
	public void onBlockExplode(BlockExplodeEvent event) {
	    List<Block> affectedBlocks = event.blockList();
	    for (Block block : affectedBlocks) {
	    	if (interactive.contains(block.getType())) event.setCancelled(true);
	    }
	}




}
