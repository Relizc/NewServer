package net.itsrelizc.modchecker.packets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.itsrelizc.modchecker.packets.rftp.DualFileTransferPacket;
import net.itsrelizc.modchecker.packets.rftp.DualInitiateFileTransfer;
import net.itsrelizc.modchecker.packets.rftp.DualSyncOneFile;
import net.itsrelizc.modchecker.server.TerminalServer.ClientHandler;

public class SocketPacket implements ISocketPacket {
	
	private ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    private DataOutputStream dataStream = new DataOutputStream(byteStream);
    protected ByteArrayInputStream byteInputStream;
    protected DataInputStream dataInputStream;
    
    protected byte[] data;

    public SocketPacket(byte[] data) {
        this.byteInputStream = new ByteArrayInputStream(data);
        this.dataInputStream = new DataInputStream(byteInputStream);
        
        this.data = data;
    }

    public SocketPacket() {}

    public void writeInt(int value) {
        try {
            dataStream.writeInt(value);
        } catch (IOException ex) {
            System.out.println("Error writing int: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public int readInt() {
        try {
            return dataInputStream.readInt();
        } catch (IOException ex) {
            System.out.println("Error reading int: " + ex.getMessage());
            ex.printStackTrace();
            return 0;
        }
    }

    public void writeLong(long value) {
        try {
            dataStream.writeLong(value);
        } catch (IOException ex) {
            System.out.println("Error writing long: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public long readLong() {
        try {
            return dataInputStream.readLong();
        } catch (IOException ex) {
            System.out.println("Error reading long: " + ex.getMessage());
            ex.printStackTrace();
            return 0;
        }
    }

    public void writeString(String value) {
        try {
            byte[] strBytes = value.getBytes("UTF-8");
            dataStream.writeInt(strBytes.length);
            dataStream.write(strBytes);
        } catch (IOException ex) {
            System.out.println("Error writing string: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public String readString() {
        try {
            int length = dataInputStream.readInt();
            byte[] strBytes = new byte[length];
            dataInputStream.readFully(strBytes);
            return new String(strBytes, "UTF-8");
        } catch (IOException ex) {
            System.out.println("Error reading string: " + ex.getMessage());
            ex.printStackTrace();
            return "";
        }
    }

    public void writeByte(byte value) {
        try {
            dataStream.writeByte(value);
        } catch (IOException ex) {
            System.out.println("Error writing byte: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public byte readByte() {
        try {
            return dataInputStream.readByte();
        } catch (IOException ex) {
            System.out.println("Error reading byte: " + ex.getMessage());
            ex.printStackTrace();
            return 0;
        }
    }

    public void write(byte[] data) {
        try {
            dataStream.write(data);
        } catch (IOException ex) {
            System.out.println("Error writing byte array: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public byte[] toByteArray() {
        try {
            dataStream.flush();
            return byteStream.toByteArray();
        } catch (IOException ex) {
            System.out.println("Error converting to byte array: " + ex.getMessage());
            ex.printStackTrace();
            return new byte[0];
        }
    }
    
    public void writePacketId(byte b) {
    	writeByte(b);
    }
    
    public byte readPacetId(byte b) {
    	return readByte();
    }

	@Override
	public void handleClient() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleServer() {
		
	}

	@Override
	public void handleServer(ClientHandler handler) {
		// TODO Auto-generated method stub
		
	}

	public static void handleServer(byte id, SocketPacket packet, ClientHandler handler) {
		if (id == (byte) 0x20) new ClientSubmitHashAssignment(packet.data).handleServer(handler);
		else if (id == (byte) 0x02) new DualInitiateFileTransfer(packet.data).handleServer(handler);
		else if (id == (byte) 0x03) new DualSyncOneFile(packet.data).handleServer(handler);
		else if (id == (byte) ClientSpigotSubmitServerInsights.ID) new ClientSpigotSubmitServerInsights(packet.data).handleServer(handler);
		else if (id == (byte) ClientSubmitPlayerCredentials.ID) new ClientSubmitPlayerCredentials(packet.data).handleServer(handler);
		else if (id == (byte) DualPingPong.ID) new DualPingPong(packet.data).handleServer(handler);
		else if (id == (byte) ServerEnigmaSubmitServerInsights.ID) new ServerEnigmaSubmitServerInsights(packet.data).handleServer(handler);
	}
	

	public static void handleClient(byte id, SocketPacket packet) {
		if (id == (byte) 0x01) new ServerConnectionUpdate(packet.data).handleClient();
		else if (id == (byte) 0x02) new DualInitiateFileTransfer(packet.data).handleClient();
		else if (id == (byte) 0x03) new DualSyncOneFile(packet.data).handleClient();
		else if (id == (byte) DualPingPong.ID) new DualPingPong(packet.data).handleClient();
		else if (id == (byte) ClientSubmitPlayerCredentials.ID) new ClientSubmitPlayerCredentials(packet.data).handleClient();
		else if (id == (byte) ClientSpigotSubmitServerInsights.ID) new ClientSpigotSubmitServerInsights(packet.data).handleClient();
		else if (id == (byte) 0x30) new DualFileTransferPacket(packet.data).handleClient();
		else if (id == (byte) ServerEnigmaSubmitServerInsights.ID) new ServerEnigmaSubmitServerInsights(packet.data).handleClient();
		
	}
}