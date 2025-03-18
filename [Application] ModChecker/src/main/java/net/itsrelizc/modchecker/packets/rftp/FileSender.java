package net.itsrelizc.modchecker.packets.rftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import net.itsrelizc.modchecker.client.TerminalClient;
import net.itsrelizc.modchecker.server.TerminalServer;

public class FileSender {

    private static final int CHUNK_SIZE = 65536; // 64KB

    public static void sendFile(String filePath, TerminalClient client) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File does not exist: " + filePath);
            return;
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[CHUNK_SIZE]; // 64KB buffer
            int bytesRead;
            int totalChunks = (int) Math.ceil(file.length() / (double) CHUNK_SIZE); // Calculate total chunks
            int chunkIndex = 0;

            while ((bytesRead = fis.read(buffer)) > 0) {
                // If the last chunk is smaller than 64KB, resize the buffer
                byte[] chunkData = bytesRead < CHUNK_SIZE ? new byte[bytesRead] : buffer;
                if (bytesRead < CHUNK_SIZE) {
                    System.arraycopy(buffer, 0, chunkData, 0, bytesRead);
                }

                DualFileTransferPacket packet = new DualFileTransferPacket(file.getName(), chunkIndex, totalChunks, chunkData);
                client.sendPacket(packet);
                chunkIndex++;
                
//                System.out.println("\tSending %s (delta %d/%d) [%d bytes]".formatted(filePath, chunkIndex, totalChunks, Math.min(file.length(),CHUNK_SIZE * chunkIndex)));
            }
        } catch (IOException ex) {
            System.out.println("Error reading file: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void sendFile(String filePath, TerminalServer.ClientHandler handler) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File does not exist: " + filePath);
            return;
        }
        
        System.out.println("Sending " + file + " (Total: %d bytes)".formatted(file.length()));

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[CHUNK_SIZE]; // 64KB buffer
            int bytesRead;
            int totalChunks = (int) Math.ceil(file.length() / (double) CHUNK_SIZE); // Calculate total chunks
            int chunkIndex = 0;

            while ((bytesRead = fis.read(buffer)) > 0) {
                // If the last chunk is smaller than 64KB, resize the buffer
                byte[] chunkData = bytesRead < CHUNK_SIZE ? new byte[bytesRead] : buffer;
                if (bytesRead < CHUNK_SIZE) {
                    System.arraycopy(buffer, 0, chunkData, 0, bytesRead);
                }

                DualFileTransferPacket packet = new DualFileTransferPacket(file.getName(), chunkIndex, totalChunks, chunkData);
                handler.sendPacket(packet);
                chunkIndex++;
                
//                System.out.println("\tSending %s (delta %d/%d) [%d bytes]".formatted(filePath, chunkIndex, totalChunks, Math.min(file.length(),CHUNK_SIZE * chunkIndex)));
            }
        } catch (IOException ex) {
            System.out.println("Error reading file: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}