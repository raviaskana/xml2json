package org.developer.xml.convertor;

import org.developer.downloader.URLDownload;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;

public class URLDownloadTest {

    File xmlFolder = null;

    @Before
    public void setUp() {
        xmlFolder = new File(System.getProperty("user.dir"),String.valueOf(System.currentTimeMillis()));
        xmlFolder.mkdir();
    }

    @After
    public void clean() {
        Arrays.stream(xmlFolder.listFiles()).forEach(file -> file.delete());
        xmlFolder.delete();
    }


    @Test
    public void downloadFile(){
        URLDownload urlDownload = new URLDownload();

        urlDownload.downloadFile("https://sxm-dataservices-samples.s3.amazonaws.com/incidents.xml.gz",xmlFolder.getAbsolutePath());
        Assert.assertTrue((Arrays.stream(xmlFolder.listFiles()).filter(file -> file.getName().endsWith(".xml")).count())> 0);
    }
}
