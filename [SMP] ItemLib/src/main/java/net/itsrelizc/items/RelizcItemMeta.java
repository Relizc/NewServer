package net.itsrelizc.items;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.itsrelizc.itemlib.RelizcItemMetas;
import net.itsrelizc.nbt.NBT.NBTTagType;



@Repeatable(value = RelizcItemMetas.class)
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.TYPE })
public @interface RelizcItemMeta {

	String key();

	NBTTagType type();

	int int_init() default 0;
	
	String str_init() default "";

	long long_init() default 0;

	double double_init() default 0.0d;
}




