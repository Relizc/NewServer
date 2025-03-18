package net.itsrelizc.modchecker.packets.rftp;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileReceiver {

    private static Map<String, FileOutputStream> fileStreams = new HashMap<>();
    private static Map<String, Integer> receivedChunks = new HashMap<>();

    public static void receiveChunk(DualFileTransferPacket packet) {
        String fileName = packet.getFileName();
        int chunkIndex = packet.getChunkIndex();
        int totalChunks = packet.getTotalChunks();
        byte[] chunkData = packet.getChunkData();
        
        System.out.println("\tReceiving %s (delta %d/%d) [%.2f%%]".formatted(fileName, chunkIndex, totalChunks, (chunkIndex * 1.0 / totalChunks) * 100));

        try {
            FileOutputStream fos = fileStreams.get(fileName);
            if (fos == null) {
                fos = new FileOutputStream(fileName);
                fileStreams.put(fileName, fos);
                receivedChunks.put(fileName, 0);
            }

            fos.write(chunkData); // Write the chunk data to the file
            receivedChunks.put(fileName, receivedChunks.get(fileName) + 1);

            if (receivedChunks.get(fileName) == totalChunks) {
                fos.close();
                fileStreams.remove(fileName);
                receivedChunks.remove(fileName);
                System.out.println("Downloaded: " + fileName);
            }
        } catch (IOException ex) {
            System.out.println("Error writing file chunk: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}