package net.itsrelizc.modchecker.packets.rftp;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.itsrelizc.modchecker.client.ModScanner;
import net.itsrelizc.modchecker.packets.ISocketPacket;
import net.itsrelizc.modchecker.packets.SocketPacket;
import net.itsrelizc.modchecker.server.TerminalServer;
import net.itsrelizc.modchecker.server.TerminalServer.ClientHandler;

public class DualSyncOneFile extends SocketPacket implements ISocketPacket {
	
	public DualSyncOneFile(String filename, String hash) {
		
		writePacketId((byte) 0x03);
		writeString(filename);
		writeString(hash);
		
	}

	public DualSyncOneFile(byte[] data) {
		this.byteInputStream = new ByteArrayInputStream(data);
        this.dataInputStream = new DataInputStream(byteInputStream);
        
        this.data = data;
	}

	@Override
	public void handleClient() {

		readByte();
		
		
		
		
	}
	
	public static Map<ClientHandler, List<String>> submitted = new HashMap<ClientHandler, List<String>>();

	@Override
	public void handleServer(ClientHandler handler) {
		readByte();
		String fn = readString();
		String hash = readString();
		
		String file = ModScanner.hashToFile.getOrDefault(hash, null);
		
		boolean exists = ModScanner.hashToFile.containsKey(hash);
		
		if (exists) {
			System.out.println("[%s] ENSURE ".formatted(handler) + hash + " : " + file);
			submitted.get(handler).add("ensure " + hash);
		} else {
			System.out.println("[%s] INVALID ".formatted(handler) + hash + " : " + file);
			submitted.get(handler).add("remove " + hash);
		}
		
	}

}
