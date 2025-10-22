package net.itsrelizc.itemlib;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;

public class ItemStackSerializer {

    // Convert ItemStack to JSONObject via Base64 NBT
    public static JSONObject serialize(ItemStack item) {
        JSONObject json = new JSONObject();
        try {
        	net.minecraft.world.item.ItemStack nms = CraftItemStack.asNMSCopy(item);
            CompoundTag tag = new CompoundTag();
            nms.save(tag);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            NbtIo.writeCompressed(tag, out);
            String base64 = Base64.getEncoder().encodeToString(out.toByteArray());

            json.put("nbt", base64);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    // Convert JSONObject (Base64 NBT) back to ItemStack
    public static ItemStack deserialize(JSONObject json) {
        try {
            if (!json.containsKey("nbt")) return new ItemStack(org.bukkit.Material.AIR);

            String base64 = (String) json.get("nbt");
            byte[] bytes = Base64.getDecoder().decode(base64);
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            CompoundTag tag = NbtIo.readCompressed(in);

            net.minecraft.world.item.ItemStack nmsItem = net.minecraft.world.item.ItemStack.of(tag);
            return CraftItemStack.asBukkitCopy(nmsItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new ItemStack(org.bukkit.Material.AIR);
        }
    }
}

