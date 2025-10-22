package net.itsrelizc.nettyservice;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;

import net.itsrelizc.modchecker.RelizcLogger;

public class Client {
	
	public static String host = "frp-bag.com"; // or server IP
    public static int port = 17758;

	
	public static String offer(String path) {
		
		
		RelizcLogger.info("Client request " + path);
	

		try (Socket socket = new Socket(host, port);
				
	             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	             InputStream in = socket.getInputStream();) {
			
			
			
			
			
	            out.println(path);

	            // Read entire response (including newlines)
	            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	            byte[] data = new byte[1024];
	            int bytesRead;

	            while ((bytesRead = in.read(data)) != -1) {
	                buffer.write(data, 0, bytesRead);
	            }

	            String response = buffer.toString("UTF-8");
	            
	            return response;

	        } catch (IOException e) {
	            e.printStackTrace();
	            
	            RelizcLogger.log(Level.SEVERE, "There was an issue contacting Relizc NettyService Servers");
	            RelizcLogger.log(Level.SEVERE, e.toString());
	        }
		return null;

	}

}
