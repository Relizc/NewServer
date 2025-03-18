package net.itsrelizc.players;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;

import net.itsrelizc.commands.RelizcCommand.TabCompleteType;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.events.TaskDelay;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;

public abstract class SignInput {
	
	
	
	private Player player;
	private String prompt;
	private TabCompleteType type;

	public SignInput(Player player, TabCompleteType valuetype, String prompt) {
		
		this.player = player;
		this.prompt = prompt;
		this.type = valuetype;
		
		
		
	}
	
	public void open() {
		
		new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

                    // create sign
                    PacketContainer blockUpdate = new PacketContainer(PacketType.Play.Server.BLOCK_CHANGE);
                    blockUpdate.getModifier().writeDefaults();
//
                    BlockPosition signPosition = new BlockPosition(player.getLocation().toVector());
                    WrappedBlockData wrappedBlockData = WrappedBlockData.createData(Material.OAK_SIGN);

                    blockUpdate.getBlockPositionModifier().write(0, signPosition);
                    blockUpdate.getBlockData().write(0, wrappedBlockData);
                    protocolManager.sendServerPacket(player, blockUpdate);

                    // edit sign
                    final Location org = player.getLocation();
                    player.sendBlockChange(org, Material.OAK_SIGN.createBlockData());
                    
                    List<String> wrapped = StringUtils.wrap(prompt, 10);
                 
                    String[] lines = new String[4];
                    lines[0] = "";
                    lines[1] = "^^^^^^^^^^^^";
                    lines[2] = Locale.get(player, "signinput.type").formatted(Locale.get(player, type.getName()));
                    lines[3] = prompt;
                    player.sendSignChange(player.getLocation(), lines);

                    // open sign editor
//                    TaskDelay.delayTask(new Runnable() {
//
//						@Override
//						public void run() {
//							
//						}
//                    	
//                    }, 0L);
                    PacketContainer openSignPacket = new PacketContainer(PacketType.Play.Server.OPEN_SIGN_EDITOR);
                    openSignPacket.getModifier().writeDefaults();
                    openSignPacket.getBooleans().write(0, true); // ensure front text
                    openSignPacket.getBlockPositionModifier().write(0, signPosition);
                    protocolManager.sendServerPacket(player, openSignPacket);
                    

                    //listen to packet update sign
            		protocolManager.addPacketListener(new PacketAdapter(EventRegistery.main, PacketType.Play.Client.UPDATE_SIGN) {
                    	@Override
                    	public void onPacketReceiving(PacketEvent event) {
                    		PacketContainer container = event.getPacket();
                    		
                    		String[] lines = container.getStringArrays().read(0);
                    		
                    		
                    		
                    		protocolManager.removePacketListener(this);
                    		
                    		player.sendBlockChange(org, org.getBlock().getBlockData());
                    	}
                    });
                    
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.runTaskLater(EventRegistery.main, 1);
		
	}
	
	public void complete(String finished) {
		throw new IllegalStateException("Override complete(String) to call a finished event!");
	}

}
