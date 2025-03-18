package net.itsrelizc.modchecker;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.io.BufferedInputStream;
import java.security.MessageDigest;
import java.security.DigestInputStream;

public class Hash {
    public static String getFileSHA1(String filePath) throws Exception {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath));
             DigestInputStream dis = new DigestInputStream(bis, sha1)) {
            
            byte[] buffer = new byte[8192]; // Read in chunks
            while (dis.read(buffer) != -1) {
                // DigestInputStream updates SHA-1 automatically
            }
        }

        // Convert byte array to hexadecimal string
        byte[] hashBytes = sha1.digest();
        return bytesToHex(hashBytes);
    }
    
    public static String getStringSHA1(String input) throws Exception {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        byte[] hashBytes = sha1.digest(input.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hashBytes);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b)); // Convert byte to hex
        }
        return hexString.toString();
    }
}


