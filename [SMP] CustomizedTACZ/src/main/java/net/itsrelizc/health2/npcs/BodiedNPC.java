package net.itsrelizc.health2.npcs;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import net.citizensnpcs.api.npc.NPC.Metadata;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.health2.Body;
import net.itsrelizc.nbt.UUIDConverter;
import net.itsrelizc.npc.RelizcNPC;

public class BodiedNPC extends RelizcNPC {
	
	public BodiedNPC() {
		super();
	}
	
	protected void setActualName(String string) {
		if (!this.npc.isSpawned()) return;
		this.npc.getEntity().getPersistentDataContainer().set(new NamespacedKey(EventRegistery.main, "bodied_name"), PersistentDataType.STRING, string);
	}
	
	protected String getActualName() {
		if (!this.npc.isSpawned()) return null;
		return this.npc.getEntity().getPersistentDataContainer().get(new NamespacedKey(EventRegistery.main, "bodied_name"), PersistentDataType.STRING);
	}
	
	protected Body getNewBody(LivingEntity entity) {
		return new Body(entity);
	}
	
	protected void setItemInInventoryRandomLocation(ItemStack it, boolean ignoreMainhand) {
		Random random = new Random();
		
		Player player = npc.getEntity() instanceof Player ? (Player) npc.getEntity() : null;
		
		int origin = ignoreMainhand ? 1 : 0;
		
		int slot = random.nextInt(origin, 36);
		while (player.getInventory().getItem(slot) != null) {
			slot = random.nextInt(origin, 36);
		}
		
		player.getInventory().setItem(slot, it);
	}
	
	@Override
	public void spawn(Location loc) {
		super.spawn(loc);
		
		npc.data().set(Metadata.DEFAULT_PROTECTED, false);
		
		if (!(npc.getEntity() instanceof LivingEntity)) return;

    	npc.data().set("uuidv2", UUIDConverter.forceUUIDv2(npc.getUniqueId()));

    	Body.parts.put(npc.data().get("uuidv2").toString(), getNewBody((LivingEntity) npc.getEntity()));
	}

}
