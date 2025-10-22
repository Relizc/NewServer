package net.itsrelizc.modchecker;

import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.util.*;

public class JarHasher {
	
	public static String expandWindowsEnv(String path) {
	    if (path.contains("%")) {
	        for (String envName : System.getenv().keySet()) {
	            path = path.replace("%" + envName + "%", System.getenv(envName));
	        }
	    }
	    return path;
	}

    public static List<String> main(String[] args) {
        // 1️⃣ Get target directory (default to current directory)
    	String dirPath;
        if (args.length > 0) {
            // Expand %APPDATA% if present
            dirPath = expandWindowsEnv(args[0]);
        } else {
            dirPath = "."; // current directory
        } 	

        File dir = new File(dirPath);
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("Invalid directory: " + dir.getAbsolutePath());
            return null;
        }

        // 2️⃣ List all JAR files
        File[] jarFiles = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".jar"));
        List<String> hashes = new ArrayList<String>();
        if (jarFiles == null || jarFiles.length == 0) {
            System.out.println("No .jar files found in: " + dir.getAbsolutePath());
            return hashes;
        }
        
        

        // 3️⃣ Compute SHA1 for each
        for (File jar : jarFiles) {
            try {
                String sha1 = sha1Hash(jar);
                //System.out.printf("%s -> %s%n", jar.getName(), sha1);
                hashes.add(sha1);
            } catch (Exception e) {
                System.err.println("Failed to hash: " + jar.getName() + " (" + e.getMessage() + ")");
            }
        }
        
        return hashes;
    }

    // 4️⃣ SHA1 hash computation
    private static String sha1Hash(File file) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");

        try (InputStream fis = Files.newInputStream(file.toPath())) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        }

        byte[] hashBytes = digest.digest();

        // Convert to hex string
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}

