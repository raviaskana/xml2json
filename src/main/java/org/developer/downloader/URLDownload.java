package org.developer.downloader;

import org.apache.log4j.Logger;
import org.developer.exception.DownloadException;
import org.developer.util.Constants;

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
    final static Logger logger = Logger.getLogger(URLDownload.class);

    @Override
    public void downloadFile(String URL, String destination) {
        logger.info("Begin:: downloadFile");
            try{
                java.net.URL downloadURL = new URL(URL);
                File download = new File(destination, Constants.FILENAME);
                ReadableByteChannel rbc = Channels.newChannel(downloadURL.openStream());
                FileOutputStream fileOutputStream = new FileOutputStream(download);
                fileOutputStream.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                extractFiles(destination);
            } catch (FileNotFoundException fnfe) {
                logger.fatal(fnfe);
                throw new DownloadException("FileNotFoundException While downloading a file",fnfe);
            } catch (MalformedURLException mfue) {
                logger.fatal(mfue);
                throw new DownloadException("MalformedURLException While downloading a file",mfue);
            } catch (IOException ioe) {
                logger.fatal(ioe);
                throw new DownloadException("IOException While downloading a file",ioe);
            }
        logger.info("End:: downloadFile");
    }

    private void extractFiles(String destination){
        logger.info("Begin:: extractFiles");

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
                logger.fatal(e);
            }
        });

        logger.info("End:: extractFiles");
    }
}
