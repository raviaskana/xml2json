package org.developer.downloader;

import org.developer.exception.DownloadException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

public class URLDownload implements IDownload {
    @Override
    public void downloadFile(String URL, String destination) {
            try{
                java.net.URL downloadURL = new URL(URL);
                String filename = "incidents.xml.gz";
                File download = new File(destination, filename);
                ReadableByteChannel rbc = Channels.newChannel(downloadURL.openStream());
                FileOutputStream fileOutputStream = new FileOutputStream(download);
                fileOutputStream.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                extractFiles(destination);
            } catch (FileNotFoundException fnfe) {
                throw new DownloadException("FileNotFoundException While downloading a file",fnfe);
            } catch (MalformedURLException mfue) {
                throw new DownloadException("MalformedURLException While downloading a file",mfue);
            } catch (IOException ioe) {
                throw new DownloadException("IOException While downloading a file",ioe);
            }

    }

    private void extractFiles(String destination){
        File compressedFolder = new File(destination);
        List<File> files = Arrays.stream(compressedFolder.listFiles()).filter(file -> file.isFile()
                            && file.getName().endsWith(".gz")).collect(Collectors.toList());
        files.forEach(file -> {
            try {
                FileInputStream fis = new FileInputStream(file);
                GZIPInputStream gzis = new GZIPInputStream(fis);
                FileOutputStream fos = new FileOutputStream(new File(destination,file.getName().replace(".gz","")));
                byte[] buffer = new byte[1024];
                int length;
                while ((length = gzis.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
            } catch (IOException e) {
                e.printStackTrace();
                //Ignore exception
            }

        });
    }
}
