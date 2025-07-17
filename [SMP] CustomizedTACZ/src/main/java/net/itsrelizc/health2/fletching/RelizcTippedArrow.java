package net.itsrelizc.health2.fletching;

import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.itemlib.RelizcNativeMinecraftItem;
import net.itsrelizc.items.RelizcItemMeta;
import net.itsrelizc.nbt.NBT;
import net.itsrelizc.nbt.NBT.NBTTagType;
import net.minecraft.nbt.CompoundTag;
	
@RelizcNativeMinecraftItem(material=Material.TIPPED_ARROW)
@RelizcItemMeta(key = "point", type = NBTTagType.TAG_Int, int_init=672418715)
@RelizcItemMeta(key = "shaft", type = NBTTagType.TAG_Int, int_init=2032762523)
@RelizcItemMeta(key = "fletching", type = NBTTagType.TAG_Int, int_init=-488142409)
@RelizcItemMeta(key = "relizceffects", type = NBTTagType.TAG_String, str_init="null")

public class RelizcTippedArrow extends RelizcNeoArrow {
	
	NamespacedKey potions = new NamespacedKey(EventRegistery.main, "effects");
	
	public static PotionEffect convertBaseDataToEffect(PotionData data) {
	    PotionType type = data.getType();
	    PotionEffectType effectType = type.getEffectType();

	    if (effectType == null) return null;

	    int duration = 0;
	    int amplifier = 0;

	    String name = type.name();

	    if (name.equals("SPEED")) {
	        duration = data.isExtended() ? 9600 : 3600;
	    } else if (name.equals("SLOWNESS")) {
	        duration = data.isExtended() ? 4800 : 1800;
	    } else if (name.equals("POISON")) {
	        duration = data.isExtended() ? 1800 : 900;
	    } else if (name.equals("REGEN")) {
	        duration = data.isExtended() ? 900 : 450;
	    } else if (name.equals("STRENGTH")) {
	        duration = data.isExtended() ? 9600 : 3600;
	    } else if (name.equals("WEAKNESS")) {
	        duration = data.isExtended() ? 9600 : 3600;
	    } else if (name.equals("FIRE_RESISTANCE")) {
	        duration = data.isExtended() ? 9600 : 3600;
	    } else if (name.equals("NIGHT_VISION")) {
	        duration = data.isExtended() ? 9600 : 3600;
	    } else if (name.equals("INVISIBILITY")) {
	        duration = data.isExtended() ? 9600 : 3600;
	    } else if (name.equals("WATER_BREATHING")) {
	        duration = data.isExtended() ? 9600 : 3600;
	    } else if (name.equals("SLOW_FALLING")) {
	        duration = data.isExtended() ? 9600 : 3600;
	    } else if (name.equals("TURTLE_MASTER")) {
	        duration = data.isExtended() ? 800 : 400;
	        amplifier = data.isUpgraded() ? 1 : 0;
	    } else if (name.equals("HEAL") || name.equals("HARM")) {
	        duration = 1;
	    } else if (name.equals("LEAPING") || name.equals("LUCK")) {
	        duration = data.isExtended() ? 9600 : 3600;
	    } else {
	        return null; // Non-effect potion (e.g. WATER, MUNDANE)
	    }

	    if (data.isUpgraded() && !name.equals("TURTLE_MASTER")) {
	        amplifier = 1;
	    }

	    return new PotionEffect(effectType, duration, amplifier);
	}



	public RelizcTippedArrow(Player owner, ItemStack it) {
		super(owner, it);
		// TODO Auto-generated constructor stub
		
		String potdata = this.getTagString("relizceffects");
		
		if (potdata.equals("null")) {
			PotionMeta meta = (PotionMeta) it.getItemMeta();
			PotionData baseData = meta.getBasePotionData(); // Base potion (e.g., POISON, LONG_POISON)
			
			PotionEffectType potEff = baseData.getType().getEffectType();
			Color c;
			if (potEff == null) {
				c = Color.fromRGB(255, 0, 255);
			} else {
				c = potEff.getColor();
			}
			
			meta.setColor(c);
			meta.setBasePotionData(new PotionData(PotionType.UNCRAFTABLE));
			for (PotionEffect effect : meta.getCustomEffects()) {
		        meta.removeCustomEffect(effect.getType());
		    }
			
			PotionEffect original = convertBaseDataToEffect(baseData);
			
			String potname; int dur; int amp;
			if (original == null) {
				potname = "UNCRAFTABLE";
				dur = 0;
				amp = 0;
			}
			else {
				potname = original.getType().getName();
				dur = original.getDuration();
				amp = original.getAmplifier();
			}
//			
//			if (original != null) {
//				meta.addCustomEffect(original, false);
//			}
			

			
			potdata = c.getRed() + "," + c.getBlue() + "," + c.getGreen() + "|MINECRAFT." + potname + ":" + dur + ":" + amp + ";";
			
		} else {
			
			String[] alp = potdata.split("|");
			String color = alp[0];
			String[] rgba = color.split(",");
			String[] eff = alp[1].split(";");
			
//			for (String str : eff) {
//				if (str.length() <= 0) continue;
//				String[] as = str.split(":");
//				
//				PotionEffectType typ ;
//				
//				String[] namespace = as[0].split(".");
//				if (namespace[0].equalsIgnoreCase("MINECRAFT")) {
//					typ = PotionEffectType.getByName(namespace[1]);
//				} else {
//					typ = null;
//				}
//				
//				PotionEffect ceff = new PotionEffect(typ, Integer.valueOf(as[1]), Integer.valueOf(as[2]));
//				
//			}
			
			PotionMeta meta = (PotionMeta) it.getItemMeta();
			Color cb = Color.fromRGB(Integer.valueOf(rgba[0]), Integer.valueOf(rgba[1]), Integer.valueOf(rgba[2]));
			meta.setColor(cb);
			meta.setBasePotionData(new PotionData(PotionType.UNCRAFTABLE));
			for (PotionEffect effect : meta.getCustomEffects()) {
		        meta.removeCustomEffect(effect.getType());
		    }
		}
		
		CompoundTag tag = NBT.getNBT(it);
		NBT.setString(tag, "relizceffects", potdata);
		ItemStack back = NBT.setCompound(it, tag);
		
		it.setItemMeta(back.getItemMeta());
		
	}
	
	@Override
	public List<String> renderInternalLore() {
		List<String> current = super.renderInternalLore();
		
		current.add(9, "potion");
		current.add(10, " ");
		
		return current;
	}

}
