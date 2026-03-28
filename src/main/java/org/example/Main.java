package org.example;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.Scanner;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;

public class Main {
    public static void main(String[] args) {

        // Phase-1

        /*
            - Take the URL input from user
            - Parse the URL
            - Determine the size of the file
         */

        System.out.println("Enter the file url here: ");
        Scanner sc = new Scanner(System.in);

        try {
            String fileUrl = sc.nextLine();

            URL url = new URL(fileUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.connect();

            long fileSize = connection.getContentLengthLong();

            System.out.println("File Size: " + fileSize + " bytes");


            // Phase-2

            /*
                - Determine the chuck which single thread load
                - Store that in the ArrayList
             */

            ArrayList<Chunk> bitsRange = new ArrayList<>();

            int thread = 4;

            int numThreads = Math.toIntExact(Math.min(thread, fileSize));
            long divRange = fileSize / numThreads;

            for (int i = 0; i < numThreads; i++) {

                long start = i * divRange;
                long end;

                if (i == numThreads - 1) {
                    end = fileSize - 1;
                } else {
                    end = start + divRange - 1;
                }

                Chunk c = new Chunk(start, end);
                bitsRange.add(c);
            }

            // Phase-3

            /*
                -
                -
             */

            ArrayList<Thread> threadList = new ArrayList<>();
            AtomicLong totalDownloaded = new AtomicLong(0);

            int index = 0;

            for (Chunk c : bitsRange) {
                String fileName = "chunk_" + index;

                Thread t = new Thread(
                        new DownloadTask(c.start, c.end, fileUrl, fileName, totalDownloaded)
                );

                threadList.add(t);   // store thread
                t.start();           // start thread

                index++;
            }

            for (Thread t : threadList) {
                try {
                    t.join();   // wait for thread to finish
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            FileOutputStream fos = new FileOutputStream("output.bin");

            byte[] buffer = new byte[8192];

            for (int i = 0; i < numThreads; i++) {
                FileInputStream fis = new FileInputStream("chunk_" + i);

                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }

                fis.close();
            }

            sc.close();
            fos.close();
            connection.disconnect();
            System.exit(0);
        }

        catch (ProtocolException e) {
            throw new RuntimeException(e);
        }

        catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}