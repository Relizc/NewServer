package net.itsrelizc.modchecker.packets;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import net.itsrelizc.modchecker.server.TerminalServer;

public class ServerConnectionUpdate extends SocketPacket implements ISocketPacket {
	
	public enum ConnectionStatus {
		
		OK((byte) 0x00),
		DISCONNECT((byte) 0x10);
		
		
		private byte status;

		private ConnectionStatus(byte status) {
			this.status = status;
		}
		
		public static ConnectionStatus getBasedOnByte(byte b) {
			
			for (ConnectionStatus status : values()) {
				if (status.status == b) return status;
			}
			return null;
			
		}
		
	}
	
	public ServerConnectionUpdate(ConnectionStatus result) {
		
		writePacketId((byte) 0x01);
		writeByte(result.status);
		
	}

	public ServerConnectionUpdate(byte[] data) {
		this.byteInputStream = new ByteArrayInputStream(data);
        this.dataInputStream = new DataInputStream(byteInputStream);
        
        this.data = data;
	}

	@Override
	public void handleClient() {
		readByte();
		
		ConnectionStatus status = ConnectionStatus.getBasedOnByte(readByte());
		System.out.println(status);
	}

	@Override
	public void handleServer() {

	}

}
