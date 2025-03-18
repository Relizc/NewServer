package net.itsrelizc.modchecker.packets.rftp;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.itsrelizc.modchecker.packets.ISocketPacket;
import net.itsrelizc.modchecker.packets.SocketPacket;
import net.itsrelizc.modchecker.server.TerminalServer.ClientHandler;

public class DualFileTransferPacket extends SocketPacket implements ISocketPacket {

    private String fileName;
    private int chunkIndex;
    private int totalChunks;
    private byte[] chunkData;

    public DualFileTransferPacket(String fileName, int chunkIndex, int totalChunks, byte[] chunkData) {
        writePacketId((byte) 0x30); // Unique ID for file transfer packets
        writeString(fileName);
        writeInt(chunkIndex);
        writeInt(totalChunks);
        writeInt(chunkData.length);
        write(chunkData); // Write the chunk data
    }

    public DualFileTransferPacket(byte[] data) {
        this.byteInputStream = new ByteArrayInputStream(data);
        this.dataInputStream = new DataInputStream(byteInputStream);
        this.data = data;

        try {
            readByte(); // Read packet ID
            fileName = readString();
            chunkIndex = readInt();
            totalChunks = readInt();
            int chunkSize = readInt();
            chunkData = new byte[chunkSize];
            dataInputStream.readFully(chunkData); // Read the chunk data
        } catch (IOException ex) {
            System.out.println("Error reading FileTransferPacket: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public String getFileName() {
        return fileName;
    }

    public int getChunkIndex() {
        return chunkIndex;
    }

    public int getTotalChunks() {
        return totalChunks;
    }

    public byte[] getChunkData() {
        return chunkData;
    }

    @Override
    public void handleClient() {
        // Handle file chunk reception on the client side
        FileReceiver.receiveChunk(this);
    }

    @Override
    public void handleServer() {
        // Handle file chunk reception on the server side
        FileReceiver.receiveChunk(this);
    }

    @Override
    public void handleServer(ClientHandler handler) {
        // Handle file chunk reception on the server side with client handler
        FileReceiver.receiveChunk(this);
    }
}