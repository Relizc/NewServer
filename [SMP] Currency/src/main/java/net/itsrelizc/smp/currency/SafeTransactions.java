package net.itsrelizc.smp.currency;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SafeTransactions {
	
	public static boolean containsAtLeast(Inventory inv, ItemStack it, int amount) {
    	int c = 0;
    	for (ItemStack item : inv) {
    		if (item == null) continue;
    		if (item.getType() == it.getType()) {
    			c += item.getAmount();
    		}
    		
    		if (c >= amount) return true;
    	}
    	
    	return false;
    }
	
	public static boolean trade(Inventory seller, Inventory buyer, ItemStack what) throws Exception {
		
		if (!containsAtLeast(seller, what, what.getAmount())) throw new Exception();
		
		int amount = what.getAmount();
		
		for (ItemStack item : seller) {
    		if (item == null) continue;
    		
    		if (item.getType() == what.getType()) {
    			int delta = Math.min(64, amount);
    			item.setAmount(item.getAmount() - delta);
    			amount -= delta;
    			
    			ItemStack n = item.clone();
    			n.setAmount(delta);
    			buyer.addItem(n);
    		}
    		
    		if (amount == 0) return true;
    	}
		
		throw new Exception();
	}

}
