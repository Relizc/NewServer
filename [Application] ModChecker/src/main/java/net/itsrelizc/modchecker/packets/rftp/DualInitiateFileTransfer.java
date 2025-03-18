package net.itsrelizc.modchecker.packets.rftp;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.itsrelizc.modchecker.client.ModScanner;
import net.itsrelizc.modchecker.client.TerminalClient;
import net.itsrelizc.modchecker.packets.ISocketPacket;
import net.itsrelizc.modchecker.packets.SocketPacket;
import net.itsrelizc.modchecker.server.TerminalServer;
import net.itsrelizc.modchecker.server.TerminalServer.ClientHandler;

public class DualInitiateFileTransfer extends SocketPacket implements ISocketPacket {
	
	public DualInitiateFileTransfer(String filename) {
		
		writePacketId((byte) 0x02);
		writeString(filename);
		
	}

	public DualInitiateFileTransfer(byte[] data) {
		this.byteInputStream = new ByteArrayInputStream(data);
        this.dataInputStream = new DataInputStream(byteInputStream);
        
        this.data = data;
	}

	@Override
	public void handleClient() {

		readByte();
		String message = readString();
		
		if (message.equalsIgnoreCase("transfer")) {
			System.out.println("Mismatched mods list! Iterating Mod results");
			
			for (Entry<String, String> stuff : ModScanner.hashToFile.entrySet()) {
				String hash = stuff.getKey();
				String file = stuff.getValue();
				
				DualSyncOneFile packet = new DualSyncOneFile(file, hash);
				TerminalClient.client.sendPacket(packet);
				
				System.out.println("Ensure " + file + " : " + hash);
			}
			
			DualInitiateFileTransfer packet2 = new DualInitiateFileTransfer("complete");
			TerminalClient.client.sendPacket(packet2);
		} else if (message.startsWith("remove")) {
			String whichTo = message.split(" ")[1];
			
			System.out.println("Removing " +  ModScanner.hashToFile.get(whichTo)+ " : " + whichTo);
			new File(ModScanner.hashToFile.get(whichTo)).delete();
		}
		
	}

	@Override
	public void handleServer(ClientHandler handler) {
		
		readByte();
		String message = readString();
		
		if ("complete".equalsIgnoreCase(message)) {
			List<String> submitted = DualSyncOneFile.submitted.get(handler);
			
			List<String> task = new ArrayList<String>();
			
			for (Entry<String, String> hashToFile : ModScanner.hashToFile.entrySet()) {
				String hash = hashToFile.getKey();
				
				boolean f = false;
				
				for (String s : submitted) {
					String h = s.split(" ")[1];
					if (h.equals(hash)) {
						f = true;
						break;
					}
				}
				
				if (!f) {
					System.out.println("[" + handler + "] NOTFOUND " + hash + " : " + hashToFile.getValue());
					task.add(hash);
				}
			}
			
			if (task.size() > 0) {
				
				System.out.println("Found " + task.size() + " missing mods from " + handler + ". Syncing...");
				
				for (String h : task) {
					
					String file = ModScanner.hashToFile.get(h);
					FileSender.sendFile(file, handler);
					
				}
				
			}
			
			System.out.println("Completed sync task for " + handler);
			List<String> result = DualSyncOneFile.submitted.get(handler);
			
			for (String s : result) {
				
				String key = s.split(" ")[0];
				if (key.equals("remove")) {
					DualInitiateFileTransfer packet = new DualInitiateFileTransfer(s);
					handler.sendPacket(packet);
				}
				
			}
		}
		
	}

}
