package net.itsrelizc.itemlib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bukkit.Material;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.TYPE })
/**
 * Marks the class as an overrider for an orignial minecraft item. This will tell the item library
 * to ignore custom textures and names.
 */
public @interface RelizcNativeMinecraftItem {
	Material material();
	
	boolean tradeable() default true;
}
