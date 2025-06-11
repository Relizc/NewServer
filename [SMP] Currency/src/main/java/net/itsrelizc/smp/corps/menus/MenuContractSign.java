package net.itsrelizc.smp.corps.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.itsrelizc.menus.ItemGenerator;
import net.itsrelizc.menus.Menu2;
import net.itsrelizc.menus.MenuTemplate2;
import net.itsrelizc.menus.Skull;
import net.itsrelizc.nbt.NBT;
import net.itsrelizc.nbt.NBT.NBTTagType;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.smp.corps.Contract;
import net.itsrelizc.smp.corps.Contract.Party;
import net.itsrelizc.smp.corps.ContractListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;


public class MenuContractSign extends MenuTemplate2 {
	
	private ItemStack contract;

	// 5 line

	public MenuContractSign(String title, ItemStack contract) {
		super(title);
		
		this.contract = contract;
	}
	
//	private static ItemStack applyTime(Player player) {
//		
//		return ItemGenerator.generate(Material.CLOCK, 1, Locale.get(player, "contract.menu.time"), null);
//		
//	}
	

	private ItemStack signNormal() {
		
		return ItemGenerator.generate(Material.LIME_DYE, 1, "§a" + Locale.get(getPlayer(), "menu.contract.sign.agree"), "§7" + Locale.get(getPlayer(), "menu.contract.sign.agree.lore"));
		
	}
	
	private ItemStack signNormalAndPrint() {
		
		return ItemGenerator.generate(Material.DIAMOND, 1, "§a" + Locale.get(getPlayer(), "menu.contract.sign.agree_print"), "§7" + Locale.get(getPlayer(), "menu.contract.sign.agree_print.lore"), " ", "§b" + Locale.get(getPlayer(), "menu.contract.sign.agree_print.lore2"));
		
	}
	
	private ItemStack refuse() {
		
		return ItemGenerator.generate(Material.RED_DYE, 1, "§c" + Locale.get(getPlayer(), "menu.contract.sign.refuse"), "§7" + Locale.get(getPlayer(), "menu.contract.sign.refuse.lore"));
		
	}
	
	@Override
	public void apply() {
		this.defaultPreset();
		
		
		this.setItem(20, signNormal());
		this.setItem(22, signNormalAndPrint());
		this.setItem(24, refuse());
		this.setItem(4, this.contract);
//		this.setItem(23, plan_c());
//		this.setItem(25, plan_c());
		
//		this.setItem();
		
	}
	
	@Override
	public void onClick(InventoryClickEvent event) {
		
		if (event.getRawSlot() == 20) {
			
			CompoundTag tag = NBT.getNBT(contract);
			
			ListTag parties = NBT.getNBTArray(tag, "party", NBTTagType.TAG_Compound);
			NBT.addItem(parties, Party.getIndividual(this.menu.getPlayer(), false).convertToNBT());
			
			NBT.setCompound(tag, "party", parties);
			
			ItemStack cur = NBT.setCompound(contract, tag);
			ContractListener.generate(cur, getPlayer());
			
			this.menu.getPlayer().setItemInHand(cur);
			
			this.menu.close();
			
		} else if (event.getRawSlot() == 22) {
			CompoundTag tag = NBT.getNBT(contract);
			
			ListTag parties = NBT.getNBTArray(tag, "party", NBTTagType.TAG_Compound);
			NBT.addItem(parties, Party.getIndividual(this.menu.getPlayer(), true).convertToNBT());
			
			NBT.setCompound(tag, "party", parties);
			
			ItemStack cur = NBT.setCompound(contract, tag);
			ContractListener.generate(cur, getPlayer());
			
			this.menu.getPlayer().setItemInHand(cur);
			
			this.menu.close();
		} else if (event.getRawSlot() == 24) {
			this.menu.close();
		}
		
	}

}
