package net.itsrelizc.modchecker.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import net.itsrelizc.modchecker.RelizcLogger;
import net.itsrelizc.modchecker.packets.*;
import net.itsrelizc.modchecker.server.TerminalServer;

public class TerminalClient {
    private Socket socket;
    private DataOutputStream output;
    private DataInputStream input;
    
    private boolean connected = false;
    
    private boolean spigot = false;

    public TerminalClient(String host, int port) {
        try {
            socket = new Socket(host, port);
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());
            System.out.println("Connected to server at " + host + ":" + port);
            connected = true;
            new Thread(this::startReceivingPackets).start(); // Start receiving packets in a separate thread
        } catch (IOException ex) {
            System.out.println("Client error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public TerminalClient(String string, int i, boolean b) {
		this(string, i);
		spigot = b;
		
	}

	public void sendPacket(SocketPacket packet) {
        try {
            byte[] data = packet.toByteArray();
            output.writeInt(data.length);
            output.write(data);
            output.flush();
//            System.out.println("Sent packet of size: " + data.length);
        } catch (IOException ex) {
            System.out.println("Error sending packet: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void startReceivingPackets() {
        try {
            while (true) {
                int packetSize = input.readInt(); // Read the packet size
                byte[] data = new byte[packetSize];
                input.readFully(data);
                
                SocketPacket packet = new SocketPacket(data);
                byte Id = packet.readByte();
                SocketPacket.handleClient(Id, packet);
//                System.out.println("Received Packet ID from server: " + Id);
            }
        } catch (IOException ex) {
            System.out.println("Error receiving packet: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

//    public void startSendingPackets() {
//        try {
//            while (true) {
//                SocketPacket packet = new SocketPacket();
//                packet.writeInt(42);
//                packet.writeString("Hello Server");
//                sendPacket(packet);
//                Thread.sleep(1000); // Send packet every second
//            }
//        } catch (InterruptedException ex) {
//            System.out.println("Packet sending interrupted: " + ex.getMessage());
//            ex.printStackTrace();
//        }
//    }

    public void close() {
        try {
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    
    public static TerminalClient client;
    
    public static boolean nodownload = false;
    
    private static String name;
    private static String uuid;
	
	public static void main(String[] args) {
		
		RelizcLogger.info("Loading client version 1.0.4");
		RelizcLogger.info("Scanning mod list...");
		
		String currentDir = System.getProperty("user.dir");
		System.out.println("Current working directory: " + currentDir);
		
		String host = null;
		int port = 0;
		
		for (int i = 0; i < args.length; i ++) {
			
			if (args[i].equalsIgnoreCase("-serverHost")) host = args[i + 1];
			else if (args[i].equalsIgnoreCase("-serverPort")) port = Integer.valueOf(args[i + 1]);
			else if (args[i].equalsIgnoreCase("-nodownload")) nodownload = true;
			else if (args[i].equalsIgnoreCase("-uniqueId")) uuid = args[i + 1];
			else if (args[i].equalsIgnoreCase("-playerName")) name = args[i + 1];
			
		}
		
		if (currentDir.endsWith("mods")) {
			
			String hash = ModScanner.scan(currentDir);
			
			System.out.println("Creating Client...");
			
			client = new TerminalClient(host, port);
			if (!client.connected) {
				RelizcLogger.severe("Unable to connect to server due to error.");
				return;
			}
			
			System.out.println("Handshaking...");
			
			ClientSubmitPlayerCredentials packet = new ClientSubmitPlayerCredentials(name, uuid);
			client.sendPacket(packet);
			
			
			System.out.println("Performing Server-Client Sync Check...");
			
			ClientSubmitHashAssignment packet21 = new ClientSubmitHashAssignment(hash);
			client.sendPacket(packet21);
			
			
			
		} else {
			
			RelizcLogger.severe("Current working directory is not a \"mods\" directory! Either specify with -modsDirectory XXX or ensure the current working system directory is in a valid mods folder");
			
			return;
		}
		
		
	}

}
