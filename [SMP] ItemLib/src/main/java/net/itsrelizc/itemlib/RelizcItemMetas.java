package net.itsrelizc.itemlib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.itsrelizc.items.RelizcItemMeta;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.TYPE })
public @interface RelizcItemMetas {
	RelizcItemMeta[] value();
}
