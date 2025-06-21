package net.itsrelizc.itemlib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.TYPE })
/**
 * NOTE: All RelizcItem annotations must be created in the class net.itsrelizc.items for it to be registered properly
 * 
 * 
 * */
public @interface RelizcItem {

	String id();

	Material material() default Material.AIR;

	boolean stackable() default true;

	Quality quality() default Quality.COMMON;

	boolean tradeable() default true;

	boolean customItemstack() default false;

	boolean placeable() default true;
	
}

@interface ExternallyTickable {
	
}

/**
 * States that this item is able to tick inside a player's inventory.<br><br>
 * 
 * Set parameter <strong>random</strong> to true to enable random ticking
 */
@interface InventoryTickable {
	
	boolean random() default true;
	
}