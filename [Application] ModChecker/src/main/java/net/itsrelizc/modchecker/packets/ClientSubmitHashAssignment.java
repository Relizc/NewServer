package net.itsrelizc.modchecker.packets;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;

import net.itsrelizc.modchecker.packets.ServerConnectionUpdate.ConnectionStatus;
import net.itsrelizc.modchecker.packets.rftp.DualInitiateFileTransfer;
import net.itsrelizc.modchecker.packets.rftp.DualSyncOneFile;
import net.itsrelizc.modchecker.packets.rftp.FileSender;
import net.itsrelizc.modchecker.server.TerminalServer;
import net.itsrelizc.modchecker.server.TerminalServer.ClientHandler;

public class ClientSubmitHashAssignment extends SocketPacket implements ISocketPacket {
	
	public ClientSubmitHashAssignment(String result) {
		
		writePacketId((byte) 0x20);
		writeString(result);
		
	}

	public ClientSubmitHashAssignment(byte[] data) {
		this.byteInputStream = new ByteArrayInputStream(data);
        this.dataInputStream = new DataInputStream(byteInputStream);
        
        this.data = data;
	}

	@Override
	public void handleClient() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleServer(ClientHandler handler) {
		readByte();
		String message = readString();
		
		if (message.equals(TerminalServer.checksumHash)) {
			
			ServerConnectionUpdate packet = new ServerConnectionUpdate(ConnectionStatus.OK);
			handler.sendPacket(packet);
			
		} else {
			
			DualInitiateFileTransfer packet = new DualInitiateFileTransfer("transfer");
			handler.sendPacket(packet);
			
			System.out.println(handler + " has mismatched mod list. Creating submission task");
			DualSyncOneFile.submitted.put(handler, new ArrayList<String>());
			
			
//			System.out.println("Sending");
			
//			FileSender.sendFile("culturaldelights-0.16.2.jar", handler);
			
		}
	}

}
