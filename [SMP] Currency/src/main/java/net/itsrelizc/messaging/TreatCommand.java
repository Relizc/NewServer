package net.itsrelizc.messaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.itemlib.ItemUtils;
import net.itsrelizc.players.locales.Locale;

public class TreatCommand extends RelizcCommand {
	
	private byte selection = 0; // 0 - general
	
	public TreatCommand() {
		super("givemealittletreat", "check ur mail");
		this.setRelizcOp(false);
		
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onPlayerExecute(Player player, String[] ntargs) {
		
//		player.sendMessage(Locale.a(player, "treat.message"));
//		player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1f, 2f);
//		
//		new BukkitRunnable() {
//
//			@Override
//			public void run() {
//				Messaging.Message msg = new Messaging.Message(
//					    Messaging.generateId(),
//					    "general",
//					    "???",
//					    "§§treat.mail.title",
//					    "§§treat.mail.message", System.currentTimeMillis(),
//					    generateRandomItems(), false, false
//					);
//				
//				Messaging.addMessage(player, msg);
//			}
//			
//		}.runTaskLater(EventRegistery.main, 20 + new Random().nextLong(20 * 3));
//		
//		
//		return true;
		return true;
	}
	
	

	
	@Override
	public TabCompleteInfo onTabComplete(CommandSender sender, String alias, String[] ntargs, Location location) {
		
		return TabCompleteInfo.presetNothing((Player) sender);
		
	}
	
	public static List<ItemStack> generateRandomItems() {
        ThreadLocalRandom rand = ThreadLocalRandom.current();

        // 1–5 different items
        int itemCount = rand.nextInt(1, 32);
        List<Material> possibleItems = Arrays.stream(Material.values())
            .filter(Material::isItem)
            .filter(m -> m.isSolid() || m.isEdible()) // filter for reasonable items
            .collect(Collectors.toList());

        List<ItemStack> items = new ArrayList<>();

        for (int i = 0; i < itemCount; i++) {
            // Pick a random material
            Material mat = possibleItems.get(rand.nextInt(possibleItems.size()));

            // Weighted amount: smaller stacks are more common
            int amount = getWeightedAmount(rand);

            items.add(ItemUtils.castOrCreateItem(new ItemStack(mat, amount)).getBukkitItem());
        }

        return items;
    }

    // Weighted amount generator (more likely to be small)
    private static int getWeightedAmount(ThreadLocalRandom rand) {
        // Use a quadratic weighting toward small numbers
        // e.g. random^2 * 64 will give more small values
        double weight = Math.pow(rand.nextDouble(), 2.2);
        int amount = (int) Math.max(1, Math.round(weight * 127));
        return amount;
    }
	
}
