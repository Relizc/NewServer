package net.itsrelizc.modchecker.server;

import java.io.*;
import java.net.*;

import net.itsrelizc.modchecker.RelizcLogger;
import net.itsrelizc.modchecker.client.ModScanner;
import net.itsrelizc.modchecker.packets.DualPingPong;
import net.itsrelizc.modchecker.packets.SocketPacket;

public class TerminalServer {
	
	private static String host = "127.0.0.1";
	private static int port = 765;
	
	private static ClientHandler clientHandler;
	
	public static void check() {
		try (ServerSocket serverSocket = new ServerSocket()) {
        	
        	serverSocket.bind(new InetSocketAddress(host, port));
        	
            System.out.println("Server is listening on " + host + ":" + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                clientHandler = new ClientHandler(socket);
                clientHandler.start();
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
	}
	
	public static String checksumHash;
	public static String currentDir = System.getProperty("user.dir");
	
    public static void main(String[] args) {

        
        for (int i = 0; i < args.length; i ++) {
        	
        	String current = args[i];
        	if (current.equalsIgnoreCase("-host")) {
        		host = args[i + 1];
        	} else if (current.equalsIgnoreCase("-port")) {
        		port = Integer.valueOf(args[i + 1]);
        	} 
        	
        }
        
        
        RelizcLogger.info("Scanning mod list...");
		
		
		System.out.println("Current working directory: " + currentDir);
		
		if (currentDir.endsWith("mods")) {
			
			checksumHash = ModScanner.scan(currentDir);
			
		} else {
			
			RelizcLogger.severe("Current working directory is not a \"mods\" directory! Either specify with -modsDirectory XXX or ensure the current working system directory is in a valid mods folder");
			
			return;
		}
        
        
		check();
        
    }
    
    public static void sendPacket(SocketPacket packet) {
    	clientHandler.sendPacket(packet);
    }
    
    
    
    
    public static class ClientHandler extends Thread {
        private Socket socket;
        private DataOutputStream output;
        private DataInputStream input;
        
        private Thread handlerThread;
        private boolean threadCompleted = false;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
        	
        	System.out.println("Created new handler for socket " + socket);
        	
        	
        	handlerThread = new Thread() {
        		
        		@Override
        		public void run() {
        			
        			
        			while (true) {
        				
        				if (threadCompleted) return;
        				
        				
        				DualPingPong packet = new DualPingPong();
        				sendPacket(packet);
        				
        				try {
    						Thread.sleep(10000);
    					} catch (InterruptedException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    						return;
    					}
        			}
        			
        			
        		}
        		
        	};
        	
        	
            try {
                input = new DataInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());
                
                handlerThread.start();

                while (true) {
                    int packetSize = input.readInt(); // Read the packet size
                    byte[] data = new byte[packetSize];
                    input.readFully(data);

                    SocketPacket packet = new SocketPacket(data);
                    
                    byte Id = packet.readByte();
                    SocketPacket.handleServer(Id, packet, this);
                    
//                    System.out.println("Received Packet ID: " + Id);
                    
                }
            } catch (IOException ex) {
                System.out.println("Server error: " + ex.getMessage() + ". Terminating handler");
//                ex.printStackTrace();
                threadCompleted = true;
            } finally {
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                threadCompleted = true;
            }
        }
        
        public void sendPacket(SocketPacket packet) {
            try {
            	byte[] data = packet.toByteArray();
                output.writeInt(data.length);
                output.write(data);
                output.flush();
            } catch (IOException e) {
            	e.printStackTrace();
            }
//            System.out.println("Sent packet of size: " + data.length);
        }
    }
}




