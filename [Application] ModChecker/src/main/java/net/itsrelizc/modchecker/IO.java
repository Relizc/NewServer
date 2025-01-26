package net.itsrelizc.modchecker;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.JOptionPane;

public class IO {
	
	public static boolean checkFolder() {
		
		if (System.getProperty("user.dir").endsWith("mods")) {
			JOptionPane.showMessageDialog(null, "This is an information message.", "Info", JOptionPane.INFORMATION_MESSAGE);

		}
		
		return false;
		
	}
	
	public static void findMods(String dir) {
		findJarFilesAndReadToml(dir);
	}
	
	public static String readModsTomlFromJar(File jarFile) {
        try (ZipFile zipFile = new ZipFile(jarFile)) {
            // Check if the "assets/mods.toml" file exists in the jar
            ZipEntry entry = zipFile.getEntry("assets/mods.toml");
            if (entry != null) {
                try (InputStream inputStream = zipFile.getInputStream(entry);
                     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    return content.toString();
                }
            } else {
                System.out.println("'assets/mods.toml' not found in " + jarFile.getName());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to find all .jar files in the given directory
    public static void findJarFilesAndReadToml(String directoryPath) {
        try {
            Files.walk(Paths.get(directoryPath))
                .filter(path -> path.toString().endsWith(".jar"))
                .forEach(path -> {
                    File jarFile = path.toFile();
                    System.out.println("Reading mods.toml from " + jarFile.getName() + ":");
                    String tomlContent = readModsTomlFromJar(jarFile);
                    if (tomlContent != null) {
                        System.out.println(tomlContent);
                    }
                });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
