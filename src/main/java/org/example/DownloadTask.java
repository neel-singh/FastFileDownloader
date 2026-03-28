package org.example;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;

public class DownloadTask implements Runnable {

    long start;
    long end;
    String fileUrl;
    String fileName;
    AtomicLong totalDownloaded;

    public DownloadTask(long start, long end, String fileUrl, String fileName, AtomicLong totalDownloaded) {
        this.start = start;
        this.end = end;
        this.fileUrl = fileUrl;
        this.fileName = fileName;
        this.totalDownloaded = totalDownloaded;
    }

    @Override
    public void run() {
        System.out.println("Downloading: " + fileName + " (" + start + "-" + end + ")");

        try {
            URL url = new URL(fileUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            String range = "bytes=" + start + "-" + end;
            connection.setRequestProperty("Range", range);
            connection.setConnectTimeout(10000);   // ← added
            connection.setReadTimeout(30000);      // ← added

            connection.connect();

            InputStream in = connection.getInputStream();
            FileOutputStream fos = new FileOutputStream(fileName);

            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);

                totalDownloaded.addAndGet(bytesRead);
            }

            fos.close();
            in.close();

            System.out.println("Finished: " + fileName);

        } catch (Exception e) {
            System.out.println("Error in thread: " + e.getMessage());
        }
    }
}