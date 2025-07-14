package net.itsrelizc.gunmod.deathutils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import net.itsrelizc.itemlib.Quality;
import net.itsrelizc.itemlib.RelizcItem;
import net.itsrelizc.itemlib.RelizcItemStack;
import net.itsrelizc.items.RelizcItemMeta;
import net.itsrelizc.menus.Skull;
import net.itsrelizc.nbt.NBT.NBTTagType;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;

@RelizcItem(id="RELIZC_PLAYER_HEAD", material=Material.PLAYER_HEAD, stackable=false, quality=Quality.UNCOMMON, tradeable=true, placeable=false)
@RelizcItemMeta(str_init = "", key = "OWNER_NAME", type = NBTTagType.TAG_String)
@RelizcItemMeta(str_init = "item.RELIZC_PLAYER_HEAD.unknown", key = "KILLER_DISPLAY", type = NBTTagType.TAG_String)
@RelizcItemMeta(str_init = "item.RELIZC_PLAYER_HEAD.unknown", key = "WEAPON_DISPLAY", type = NBTTagType.TAG_String)
@RelizcItemMeta(long_init = 0, key = "OWNER_RANK", type = NBTTagType.TAG_Long)
@RelizcItemMeta(long_init = 0, key = "DEATH_TIME", type = NBTTagType.TAG_Long)
public class RelizcOverridedPlayerHead extends RelizcItemStack {

	public RelizcOverridedPlayerHead(Player owner, ItemStack it) {
		super(owner, it);
		// TODO Auto-generated constructor stub
		//this.setMetadata("", it);
		
		SkullMeta meta = (SkullMeta) it.getItemMeta();
		meta.setOwnerProfile(owner.getPlayerProfile());
		
		if (this.getTagLong("OWNER_RANK") >= 2) {
			
			meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
			
		}
		
		it.setItemMeta(meta);
		
		
	}
	
	@Override
	public List<String> renderInternalLore() {
		
		String cringe = Locale.a(getOwner(), "item.RELIZC_PLAYER_HEAD.description");
		
		//cringe += "\n" +Locale.get(getOwner(), "item.RELIZC_PLAYER_HEAD.description." + this.getTagLong("OWNER_RANK"));
		
		List<String> ass = StringUtils.fromArgs(
				cringe.split("\n")
				);
		
		ass.add(" ");
		ass.add(Locale.get(getOwner(), "item.RELIZC_PLAYER_HEAD.whokilled").formatted(Locale.a(getOwner(), this.getTagString("KILLER_DISPLAY"))));
		ass.add(Locale.get(getOwner(), "item.RELIZC_PLAYER_HEAD.weaponkilled").formatted(Locale.a(getOwner(), this.getTagString("WEAPON_DISPLAY"))));
		ass.add(Locale.get(getOwner(), "item.RELIZC_PLAYER_HEAD.deathtime").formatted(convertEpochToReadable(this.getTagLong("DEATH_TIME"))));
		
		////(cringe);
		
		return ass;
		
	}
	
	private static String convertEpochToReadable(long epochMillis) {
        Instant instant = Instant.ofEpochMilli(epochMillis);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault()); // or use ZoneId.of("UTC") for UTC time
        return formatter.format(instant);
    }

	@Override
	public String renderName() {
		return super.renderName().formatted(this.getTagString("OWNER_NAME"));
	}

}
