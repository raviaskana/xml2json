package org.developer.process;

import org.developer.downloader.IDownload;
import org.developer.downloader.URLDownload;
import org.developer.xml.convertor.XML2JsonConvertor;
import org.developer.xml.convertor.XMLConvertor;

public class Processor {

    public static final String DOWNLOAD_URL = "https://sxm-dataservices-samples.s3.amazonaws.com/incidents.xml.gz";
    public void process(){
        IDownload download = new URLDownload();
        XMLConvertor xmlConvertor = new XML2JsonConvertor();
        String tempFolder = System.getProperty("user.dir");
        download.downloadFile(DOWNLOAD_URL,tempFolder);
        xmlConvertor.convertXML(tempFolder);
    }
}
