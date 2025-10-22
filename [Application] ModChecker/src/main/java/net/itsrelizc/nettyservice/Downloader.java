package net.itsrelizc.nettyservice;

import java.io.*;
import java.net.*;

import java.io.*;
import java.net.*;

public class Downloader {

    /**
     * Downloads a file from a given URL to a destination directory,
     * printing progress every 0.5 seconds.
     */
    public static File downloadFile(String urlString, String destDir) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", "JavaDownloader/1.0");
        connection.connect();

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP error: " + responseCode + " - " + connection.getResponseMessage());
        }

        long contentLength = connection.getContentLengthLong();
        if (contentLength <= 0) {
            System.out.println("Warning: unknown file size (no Content-Length header). Progress may be inaccurate.");
        }

        // Get filename
        String fileName;
        String disposition = connection.getHeaderField("Content-Disposition");
        if (disposition != null && disposition.contains("filename=")) {
            fileName = disposition.split("filename=")[1].replace("\"", "");
        } else {
            fileName = url.getPath().substring(url.getPath().lastIndexOf('/') + 1);
        }

        // Ensure directory exists
        File dir = new File(destDir);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Failed to create directory: " + destDir);
        }

        File outputFile = new File(dir, fileName);
        System.out.println("Downloading to: " + outputFile.getAbsolutePath());

        // Start download
        try (InputStream in = connection.getInputStream();
             FileOutputStream out = new FileOutputStream(outputFile)) {

            byte[] buffer = new byte[8192];
            int bytesRead;
            long totalRead = 0;
            long lastPrintTime = System.currentTimeMillis();

            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                totalRead += bytesRead;

                long now = System.currentTimeMillis();
                if (now - lastPrintTime >= 500) { // print every 0.5s
                    if (contentLength > 0) {
                        double percent = (totalRead * 100.0) / contentLength;
                        System.out.printf("Progress: %.2f%% (%d / %d bytes)%n",
                                percent, totalRead, contentLength);
                    } else {
                        System.out.printf("Downloaded: %d bytes%n", totalRead);
                    }
                    lastPrintTime = now;
                }
            }

            // Final update
            if (contentLength > 0) {
                System.out.printf("Download complete: %.2f%% (%d / %d bytes)%n",
                        100.0, totalRead, contentLength);
            } else {
                System.out.printf("Download complete: %d bytes%n", totalRead);
            }
        } finally {
            connection.disconnect();
        }

        return outputFile;
    }

    // Example usage
    public static void main(String[] args) {
        try {
            downloadFile("https://example.com/test.jar", "./downloads");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


