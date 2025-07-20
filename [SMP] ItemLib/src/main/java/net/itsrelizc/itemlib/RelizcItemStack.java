package net.itsrelizc.itemlib;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.itsrelizc.nbt.NBT;
import net.itsrelizc.players.Profile;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.players.locales.Locale.Language;
import net.minecraft.nbt.CompoundTag;

/**
 * A custom Relizc SMP class containing metadata for original Bukkit items. This intended to look like and feel like Minecraft Forge's item API and 
 * can create items with decorators, despite the fact that the user have to register it during initialization.
 * 
 * 
 * Please note that this class does not extend Bukkit ItemStack nor Mojang's ItemStack. Please use {@link #getBukkitItem()} to convert it to 
 * Bukkit items, which is safe enough to keep its essential RelizcItem NBT tags and convert it back later.
 * 
 * When comparing RelizcItemStacks, do not use double equals operator. Instead, please use {@link #equals(Object)}
 * 
 * @see org.bukkit.inventory.ItemStack
 */
public class RelizcItemStack {
	
	private ItemStack bukkit;
	protected Player owner;
	private Language renderedLangauge;	
	private UUID uniqueId;
	
	private int referenceSlot = -1;
	
	public RelizcItemStack(Player owner, ItemStack it) {
		bukkit = it;
		this.owner = owner;
		
		
		this.renderedLangauge = Language.valueOf(NBT.getString(it, "lang"));
		this.uniqueId = getUniqueId();
		
		
		
		// updateReferenceSlot(); DO NOT CALL THIS or else it will be marked "outside-inventory"
	}
	
	/**
	 * Basically casts this RelizcItemStack to regular Bukkit ItemStack. The NBT contents will remain
	 * @return Bukkit ItemStack
	 */
	public ItemStack getBukkitItem() {
		return bukkit;
	}
	
	/**
	 * Gets the namespace ID of this item. This is declared in the annotation of the Item class with
	 * the annotation key "id"
	 * 
	 * @return The namespace ID of this item.
	 */
	public String getID() {
		return this.getTagString("id");
	}
	
	/**
	 * Gets the custom name of this Relizc ItemStack. Note that this does not return the Bukkit's custom name.
	 * 
	 * @return The custom name of this item, or null if there is no name.
	 */
	public String getCustomName() {
		
		return this.getTagString("relizcCustomName");
		
	}
	
	/**
	 * Gets the quality of this item.
	 * @return The quality of this item
	 */
	public Quality getQuality() {
		// TODO Auto-generated method stub
		RelizcItem annotation = this.getClass().getAnnotation(RelizcItem.class);
		return annotation.quality();
	}
	
	/**
	 * Updates the reference slot in a player's inventory for this item. Only used to take care of
	 * NBT changes by instance changes. This will not work on stackable items or items that cannot
	 * hold metadata.<br><br>
	 * 
	 * This is done by looping through the player's inventory. For optimization purposes, items that
	 * do not tick outside a player's inventory will not have its reference slots updated when dropped.
	 * To make it the item tick outside an inventory, add the annotation {@code ExternallyTickable}<br><br>
	 * 
	 * For items that do tick outside a player's inventory, the reference slot will be -1, but the field
	 * referenceEntity will be presented.<br><br>
	 * 
	 * Nevertheless, this function should be ignored for regular API users. This is unused unless 
	 * under special circumstances that the reference slots have to be updated.<br><br>
	 */
	public void updateReferenceSlot() {
		
		for (int i = 0; i < owner.getInventory().getSize(); i ++) {
			ItemStack content = owner.getInventory().getItem(i);
			
			// //("Going over slot " + i + " with " + content);
			
			if (content == null || content.getType() == Material.AIR) continue;
			
			
			
			net.minecraft.world.item.ItemStack it = CraftItemStack.asNMSCopy(content);
			if (!it.getOrCreateTag().hasUUID("uid")) continue;
			UUID the = it.getOrCreateTag().getUUID("uid");
			
			// //("Mines " + getUniqueId() + " " + the + " " + the.equals(getUniqueId()));
			
			if (getUniqueId().equals(the)) {
				this.referenceSlot = i;
				this.bukkit = content;
				
				return;
			}
		}
		
		this.referenceSlot = -2; // unable to find in player inventory
	}
	
	/**
	 * Gets the Unique ID of this item when created. Note that this only works for RelizcItems or regular 
	 * mojang items that are considered unstackable.
	 * @return The Unique ID of this unstackable item. Null if this item is unstackable
	 * @see java.util.UUID
	 */
	public UUID getUniqueId() {
		net.minecraft.world.item.ItemStack it = CraftItemStack.asNMSCopy(bukkit);
		try {
			return it.getTag().getUUID("uid");
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 
	 * Used when the item annotation has customItemstack set to true. This function is used to generate
	 * a itemstack for this specific item.
	 * 
	 * @return itemStack.
	 */
	public ItemStack getGeneratedItemStack(Player player) {
		return null;
	}
	
	/**
	 * Gets the player that is currently holding/using this item. This is intended for multilingual purposes
	 * @return The player
	 */
	public Player getOwner() {
		return owner;
	}
	
	/**
	 * Gets the language that the lore visuals of this item is rendered in.
	 * @return The language
	 * @see net.itsrelizc.players.locales.Locale.Language
	 */
	public Language getRenderedLanguage() {
		return renderedLangauge;
	}
	
	/**
	 * An function that is REQUIRED and RECOMMENDED to be overridden. Allows user to add custom user rendering by code.
	 */
	public List<String> renderInternalLore() {
		List<String> additional = new ArrayList<String>();
		if (this.getBukkitItem().getEnchantments().size() > 0) {
			additional.add(" ");
		}
		return additional;
	}
	
	
	/**
	 * Equivalent of getting a integer NBT of this item's NBT.
	 * @param string The NBT key
	 * @return The Integer NBT value
	 */
	public Integer getTagInteger(String string) {
		return CraftItemStack.asNMSCopy(bukkit).getTag().getInt(string);
	}
	
	protected CompoundTag getTag() {
		return CraftItemStack.asNMSCopy(bukkit).getOrCreateTag();
	}
	
	public double getTagDouble(String string) {
		// TODO Auto-generated method stub
		return CraftItemStack.asNMSCopy(bukkit).getTag().getDouble(string);
	}
	
	private ItemStack _nGetHelper(int slot) {
		if (slot < 0 || slot >= owner.getInventory().getSize()) return null;
		return owner.getInventory().getItem(referenceSlot);
	}
	
	/**
	 * Check if the stored bukkit item in this container is the same
	 * as the referenced item.
	 */
	private void checkIfBukkitItemIsReferenced() {
		if (this.referenceSlot == -2) return; // not in inventory
		else if (this.referenceSlot == -1) {
			this.updateReferenceSlot(); // sets to -2 if not found
		}
		else if (!this.bukkit.equals(_nGetHelper(this.referenceSlot))) {
			this.updateReferenceSlot();
		} 
		
		// //(this.referenceSlot + " ");
	}
	
	/**
	 * Sets a metadata NBT integer tag. This directly updates the item in the owner's inventory
	 * @param string The NBT key
	 * @param value The value to set
	 */
	protected void setTagInteger(String string, Integer value) {
		checkIfBukkitItemIsReferenced();
		
		net.minecraft.world.item.ItemStack is = CraftItemStack.asNMSCopy(bukkit);
		CompoundTag tag = is.getTag();
		tag.putInt(string, value);
		is.setTag(tag);
		ItemStack finished = CraftItemStack.asBukkitCopy(is);
		////(" " + this.referenceSlot);
		owner.getInventory().setItem(referenceSlot, finished);
		bukkit = finished;
	}
	
	protected void setTagString(String string, String value) {
		checkIfBukkitItemIsReferenced();
		
		net.minecraft.world.item.ItemStack is = CraftItemStack.asNMSCopy(bukkit);
		CompoundTag tag = is.getTag();
		tag.putString(string, value);
		is.setTag(tag);
		ItemStack finished = CraftItemStack.asBukkitCopy(is);
		////(" " + this.referenceSlot);
		owner.getInventory().setItem(referenceSlot, finished);
		bukkit = finished;
	}
	
	public Long getTagLong(String string) {
		return CraftItemStack.asNMSCopy(bukkit).getTag().getLong(string);
	}
	
	public String getTagString(String string) {
		return CraftItemStack.asNMSCopy(bukkit).getTag().getString(string);
	}
	
	/**
	 * Clears the lore cache and re-renders all the lores, names, and languages
	 * of this item. Best used when a item name or lore requires a metadata and
	 * the metadata is updated.<br><br>
	 * 
	 * <strong>No need to call this after calling {@link #setMetadata(String, Object)} because this function automatically updates the lore</strong>
	 */
	protected void renderDisplay() {
		checkIfBukkitItemIsReferenced();
		
		RelizcItem annotation = this.getClass().getAnnotation(RelizcItem.class);
		
		Language lang;
		if (owner == null) lang = Language.ZH_CN;
		else lang = Profile.findByOwner(owner).lang;
		
		ItemUtils.renderNames(annotation, bukkit, owner, lang);
		
		ItemMeta meta2 = bukkit.getItemMeta();	
		
		List<String> l = meta2.getLore();
		List<String> rendered = this.renderInternalLore();
		rendered.forEach(s -> l.add(s));
		
		
		
		if (rendered.size() == 0) {
			l.remove(l.size() - 1);
		}
		
		meta2.setLore(l);
		_nGetHelper(this.referenceSlot).setItemMeta(meta2);
	}
	
	/**
	 * Sets a defined metadata of this item.
	 * @param key The metadata key
	 * @param value The value to set
	 * 
	 * @deprecated
	 */
	public void setMetadata(String key, Object value) {
		
	}
	
	/**
	 * Essentially checks if the unique IDs of both RelizcItemStacks are equal. If the items are stackable, returns if
	 * the Bukkit item of both RelizcItems are byte equal.
	 * {@inheritDoc Object#equals(Object)}
	 */
	@Override
	public boolean equals(Object other) {
		
		if (!(other instanceof RelizcItemStack)) return false;
		
		RelizcItemStack the = (RelizcItemStack) other;
		if (getUniqueId() == null) {
			return the.getBukkitItem() == bukkit;
		}
		return the.getUniqueId().equals(getUniqueId());
		
	}
	
	/**
	 * Renders the name of this item. Best used if the item's name requires formatting arguments
	 * or is different from the intended / original item name.
	 * 
	 * @return A item name
	 */
	protected String renderName() {
		// TODO Auto-generated method stub
		String id = getID();
		if (id.toLowerCase().startsWith("minecraft")) {
			return Locale.getMojang(owner, "item.minecraft." + id.substring(10).toLowerCase());
		} else {
			return Locale.get(owner, "item." + getID().toLowerCase() + ".name");
		}
		
		
	}

}
