package net.itsrelizc.dimensions.end;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.citizensnpcs.api.event.NPCClickEvent;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.npc.LookAtPlayerTask;
import net.itsrelizc.npc.NPCDialogueSession;
import net.itsrelizc.npc.NPCDialogueSessionHandler;
import net.itsrelizc.npc.RelizcNPC;
import net.itsrelizc.players.locales.Locale;

public class NPCProfBrown extends RelizcNPC {
	
	public static class NPCListener implements Listener {

	    private final Plugin plugin;
		private NPC self;
		private RelizcNPC self2;
		
	    public NPCListener(Plugin plugin, NPC self, RelizcNPC self2) {
	    	this.plugin = plugin;
	        this.self = self;
	        this.self2 = self2;
	    }

	    @EventHandler
	    public void onNPCRightClick(NPCRightClickEvent event) {
	        int npcId = event.getNPC().getId();
	        
	        if (npcId != self.getId()) return;
	        
	        self2.click(event);
	    }

	    @EventHandler
	    public void onNPCLeftClick(NPCLeftClickEvent event) {
	    	int npcId = event.getNPC().getId();
	        
	        if (npcId != self.getId()) return;
	        
	        self2.click(event);
	    }
	}
	
	@Override
	public String getRealName() {
		return "npc.brown";
	};
	
	private static String skinValue = "ewogICJ0aW1lc3RhbXAiIDogMTc1ODQwMDA0MDM0OSwKICAicHJvZmlsZUlkIiA6ICIzZGE2ZDgxOTI5MTY0MTNlODhlNzg2MjQ3NzA4YjkzZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJGZXJTdGlsZSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83OWM4MTliNTFiNGI4Mzg5YTllNWRmYjg5MWRjNjQ3NjQ1OGExZTYyOTI5Yjk1ZWVmNzk5NTg2NjUwZDE5YmFmIgogICAgfQogIH0KfQ==";
	private static String skinSignature = "thgb3Gm74CMBUqrq8sAvWuSKKerT2Xl8BrDJ7C/5DxHkgmQNeXil5LbncFP0vtLFsH9On+qsSbCYbuDsUG1HVp5VtdAX8sqjZTMrR+Zw4KF4tPIkFGCG5ggvxgl19TB9aXCoXCx+h6fgKeMWwsdemQBMwzBA3LIGBCdihB/qLqqSJyLj6tkrvNo4npLqchsyYgCyC7fW3wiYhhSodGNCtrL/wUVPlbnXbjOQd94FjZsXlfM8CpEPdML8AiepXEgHKq6q3GJlYbed9F/5BZXr0pzD2xsWvWZsfUo5sQjHqs5hGP5ijPS4U2B3lMvFff4sdMtzRACV/2RjRzvdJndhEDPN1ZZJov178T6vMSoumZ6QwVD0d2AjIat5nQuyFjqbXzLCjItQ85ncmGmO5Zc6grcH50SuX2Pr9sxiixq3WsYS4gmgnzo1QeHRO9IRpnX59JhQN6fvW3N60poJHvwLjIAMnWxxrmgv9ZwET1jtQLSs26JaJX6qAW+ruhTonr4XwjG8FV01dqSjw9gm41NcWEsjuPq8QBINoB6H72sZEwLeBZ3Rkx5KatXKEdOc5NgtumFHQb2/cFd0Kb09XCZ0nTe7x9gUym9K/ADgcQAK3LYWCcia/CjmxtWdnLF76/hyz3LffLv9klb3DpGIHg8Kv5QtkxjyW45HSmhvluQ9E4Q=";
	
	public static void spawnme() {
		NPCProfBrown guy = new NPCProfBrown();
		
		guy.spawn(new Location(Bukkit.getWorld("world"), -97.5, 65, -50.5, 180f, 0f));
	}

	private Location lookloc;
	
	@Override
	public void spawn(Location loc) {
		super.spawn(loc);
		
		this.lookloc = loc.clone();
		
		EventRegistery.register(new NPCProfBrown.NPCListener(EventRegistery.main, npc, this));
		
		
		LookAtPlayerTask.scheduleFor(EventRegistery.main, npc, 8, 5l, lookloc);
		
		Player bukkitPlayer = (Player) npc.getEntity();
		
		net.minecraft.world.entity.player.Player nmsPlayer = ((CraftPlayer) bukkitPlayer).getHandle();

        // Get the GameProfile
        GameProfile profile = nmsPlayer.getGameProfile();

        // Remove old textures property
        profile.getProperties().removeAll("textures");

        // Add new textures property
        profile.getProperties().put("textures", new Property("textures", skinValue, skinSignature));
	}
	
	NPCDialogueSessionHandler<BrownishNPCDialogueSession> handler = new NPCDialogueSessionHandler<BrownishNPCDialogueSession>(this, BrownishNPCDialogueSession.class);
	
	
	public static class BrownishNPCDialogueSession extends NPCDialogueSession {

		public BrownishNPCDialogueSession(Player target, RelizcNPC who, long expiresSeconds) {
			super(target, who, expiresSeconds);
			// TODO Auto-generated constructor stub
		}
		
		
		private void talkshit(int index) {
			
			String[] dialogue0 = Locale.a(getPlayer(), "npc.brown.dialogue0").split("\n");
			
			//this.waitForResponse(getPlayer(), Arrays.asList(new Response("ask_0", "npc.brown.ask0.ask0")), 20);
			
			if (index == 0) {
				this.getNPC().sayDialoguePrivateChat(getPlayer(), "...");
			} else if (index >= 1 && index <= 2) {
				this.getNPC().sayDialoguePrivateChat(getPlayer(), dialogue0[index - 1]);
			} else if (index == 3) {
				this.waitForResponse(getPlayer(), Arrays.asList(new Response("ask_0", "npc.brown.ask0.ask0")), 20);
			}
			
			
		}
		
		public void startTalking() {

			new BukkitRunnable() {
				
				int index = 0;

				@Override
				public void run() {
					talkshit(index);
					index ++;
					
					if (index == 4) {
						this.cancel();
						return;
					}
				}
				
			}.runTaskTimer(EventRegistery.main, 0l, 30l);
		}
	}
	
	@Override
	public void click(NPCClickEvent event) {
		Player player = event.getClicker();
		
		if (handler.hasSession(player)) return;
		
		BrownishNPCDialogueSession session = handler.getSession(player, 5);
		session.startTalking();
	}

}
