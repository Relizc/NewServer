package net.itsrelizc.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.itsrelizc.itemlib.Quality;
import net.itsrelizc.itemlib.RelizcItem;
import net.itsrelizc.itemlib.RelizcItemStack;
import net.itsrelizc.nbt.NBT.NBTTagType;

@RelizcItem(id="MASTER_BAITER", material=Material.STRING, stackable=false, quality=Quality.EPIC, tradeable=true)
@RelizcItemMeta(key="DAYSTREAK", type=NBTTagType.TAG_Int, int_init=0)
public class RelizcMasterBaiter extends RelizcItemStack {

  public RelizcMasterBaiter(Player owner, ItemStack it) {
    super(owner, it);
  }

  @Override
  public List<String> renderInternalLore() {
    // TODO: Add dynamic lore rendering logic
    return new ArrayList<>();
  }
}