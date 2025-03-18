package net.itsrelizc.modchecker.client;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.*;

import org.json.simple.JSONObject;

import net.itsrelizc.modchecker.Hash;

public class ModScanner {
	
	private static String content = "";
	private static int mods = 0;
	
	public static String scan(String directory) {
		
		System.out.println("Processing mods in " + directory);
		
		Path currentDir = Paths.get(directory);

        try {
			Files.list(currentDir)
			     .filter(path -> path.toString().endsWith(".jar"))
			     .forEach(ModScanner::processJarFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        try {
			content = Hash.getStringSHA1(content);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("Found " + mods + " mods. Checksum Hash: " + content);
        return content;
	}
	
	public static Map<String, String> hashToFile = new HashMap<String, String>();
	
	private static void processJarFile(Path jarPath) {
        try (ZipFile zipFile = new ZipFile(jarPath.toFile())) {
//            System.out.println("Processing: " + jarPath.getFileName());
        	
        	String hash = Hash.getFileSHA1(jarPath.toString());
        	content += hash;

            // Try reading Forge (mods.toml)
            ZipEntry tomlEntry = zipFile.getEntry("META-INF/mods.toml");
            if (tomlEntry != null) {
//                System.out.println("  - Detected Forge mod (mods.toml)");
                String result = readTOML(zipFile, tomlEntry);
                System.out.println("\tFORGE " + jarPath.getFileName() + " -> " + result);
//                System.out.println(hash);
                mods ++;
                hashToFile.put(hash, jarPath.getFileName().toString());
                return;
            }



//            System.out.println("  - No known metadata found.");

        } catch (Exception e) {
            System.err.println("Error reading " + jarPath.getFileName() + ": " + e.getMessage());
        }
    }

    private static String readTOML(ZipFile zipFile, ZipEntry entry) throws IOException {
        try (InputStream is = zipFile.getInputStream(entry);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
        	
        	boolean readingModInfo = false;
        	
        	Map<String, String> tomlData = new HashMap<>();
            
            Pattern pattern = Pattern.compile("([a-zA-Z0-9_]+)\\s*=\\s*(\"[^\"]*\"|[^\\s#]+)"); 



            String line;
            while ((line = reader.readLine()) != null) {
            	
            	
            	line = line.trim();
            	
            	
            	
            	if (readingModInfo) {
            		
            		Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        String key = matcher.group(1);
                        String value = matcher.group(2).replace("\"", ""); // Remove quotes from strings
                        tomlData.put(key, value);
                    }
            		
            	}
            	
            	if (line.startsWith("[[")) {
            		if (line.startsWith("[[mods]]")) {
            			readingModInfo = true;
            		} else {
            			readingModInfo = false;
            			break;
            		}
            	}
            	
                
            }
            
//            System.out.println("Mod ID: " + tomlData.get("modId"));
//            System.out.println("Display Name: " + tomlData.get("displayName"));
//            System.out.println("Authors: " + tomlData.get("authors"));
            
            return "[ %s | %s ]".formatted(tomlData.get("modId"), tomlData.get("displayName"));
        }
    }

    

}
