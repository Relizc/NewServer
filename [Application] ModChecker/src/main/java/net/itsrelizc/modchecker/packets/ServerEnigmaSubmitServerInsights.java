package net.itsrelizc.modchecker.packets;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.itsrelizc.events.TaskDelay;
import net.itsrelizc.modchecker.client.TerminalClient;
import net.itsrelizc.modchecker.plugin.MainPlugin;
import net.itsrelizc.modchecker.server.TerminalServer;
import net.itsrelizc.modchecker.server.TerminalServer.ClientHandler;
import net.itsrelizc.players.locales.Locale;

public class ServerEnigmaSubmitServerInsights extends SocketPacket implements ISocketPacket {
	
	public static final byte ID = (byte) 0x24;
	
	public static enum EnigmaInsightType {
		
		KICK((byte) 0x00);
		
		private byte header;

		private EnigmaInsightType(byte header) {
			this.header = header;
		}
		
	}
	
	public ServerEnigmaSubmitServerInsights(EnigmaInsightType type, String playerName, String uniqueId) {
		
		writePacketId(ID);
		writeByte(type.header);
		writeString(playerName);
		writeString(uniqueId);
		
	}

	public ServerEnigmaSubmitServerInsights(byte[] data) {
		this.byteInputStream = new ByteArrayInputStream(data);
        this.dataInputStream = new DataInputStream(byteInputStream);
        
        this.data = data;
	}

	@Override
	public void handleClient() { // Bukkit Client
		readByte();
		
		byte type = readByte();
		System.out.println("Prepare for disconnect");
		
		if (type == EnigmaInsightType.KICK.header) {
			
			String name = readString();
			UUID uuid = UUID.fromString(readString());
			
			Player player = Bukkit.getPlayer(uuid);
			System.out.println(player);
			
//			String msg = Locale.get(player, "enigma.disconnected");
			
			Thread kicker = new Thread() {

				@Override
				public void run() {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					player.kickPlayer("§cEnigma加载不正常或断开连接。请检查Enigma是否正常启动 (通常为一个黑色的命令窗口)，或重新链接。若还出现此问题，请重启游戏。");
				}
				
			};
			
			kicker.start();
		}

	}

	@Override
	public void handleServer(ClientHandler handler) {
		readByte();
		
		
	}

}
