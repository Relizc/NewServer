package net.itsrelizc.smp.insurance;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.itsrelizc.menus.ItemGenerator;
import net.itsrelizc.menus.Menu2;
import net.itsrelizc.menus.MenuTemplate2;
import net.itsrelizc.menus.Skull;
import net.itsrelizc.players.locales.Locale;


public class InsuranceMenu extends MenuTemplate2 {
	
	private org.bukkit.inventory.ItemStack plan_a() {
		
		return ItemGenerator.generate(Material.CRAFTING_TABLE, 1, "§6" + Locale.get(this.getPlayer(), "item.insurance_plan_craft.name"), "§7" + Locale.get(this.getPlayer(), "item.insurance_plan_craft.lore"), " ", "§8§m------------", "§7• " + Locale.get(this.getPlayer(), "insurance.notify.onlyone"), "§7• " + Locale.get(this.getPlayer(), "insurance.notify.priceplan").formatted(Insurance.CRAFT_PRICE / 1000.0f), "§7• " + Locale.get(this.getPlayer(), "insurance.notify.pricevary"), "§7• " + Locale.get(this.getPlayer(), "economy.no_tax"), " ", "§a" + Locale.get(this.getPlayer(), "insurance.select"));
	}
	
	private org.bukkit.inventory.ItemStack plan_b() {
		
		ItemStack x = ItemGenerator.generate(Material.DIAMOND_PICKAXE, 1, "§a" + Locale.get(this.getPlayer(), "item.insurance_resource.name"), "§7" + Locale.get(this.getPlayer(), "item.insurance_resource.lore"), " ", "§8§m------------", "§7• " + Locale.get(this.getPlayer(), "insurance.notify.onlyone"), "§7• " + Locale.get(this.getPlayer(), "insurance.notify.priceplan").formatted(Insurance.RESOURCE_PRICE / 1000.0f), "§7• " + Locale.get(this.getPlayer(), "insurance.notify.pricenovary"), "§7• " + Locale.get(this.getPlayer(), "economy.no_tax"), " ", "§a" + Locale.get(this.getPlayer(), "insurance.select"));
		ItemMeta im = x.getItemMeta();
		im.addItemFlags(ItemFlag.values());
		x.setItemMeta(im);
		return x;
		
	}
	
	private org.bukkit.inventory.ItemStack plan_c() {
		
		return ItemGenerator.generate(Material.GRAY_DYE, 1, Locale.get(this.getPlayer(), "item.insurance_plan_notopen.name"));
	}
	
	private ItemStack description() {
		return ItemGenerator.generate(Skull.getCustomSkull("http://textures.minecraft.net/texture/2ec76e301b2c9cf96a96cd10b7312a62ca3aef2e23918b5b69474b5051f559b"), 
				1, 
				Locale.get(this.getPlayer(), "item.insurance_justin.name"), 
				null);
		
	}

	public InsuranceMenu(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void apply() {
		this.defaultPreset();
		
		
		this.setItem(19, plan_a());
		this.setItem(21, plan_b());
		this.setItem(23, plan_c());
		this.setItem(25, plan_c());
		
	}

}
