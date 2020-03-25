package org.developer.process;

import org.apache.log4j.Logger;
import org.developer.downloader.IDownload;
import org.developer.downloader.URLDownload;
import org.developer.util.Constants;
import org.developer.util.Utility;
import org.developer.xml.convertor.XML2JsonConvertor;
import org.developer.xml.convertor.XMLConvertor;

public class Processor {
    final static Logger logger = Logger.getLogger(Processor.class);

    public void process(){
        logger.info("Begin :: process");
        String tempFolder = null;
        try {
            IDownload download = new URLDownload();
            XMLConvertor xmlConvertor = new XML2JsonConvertor();
            tempFolder = Utility.getTempFolder();
            download.downloadFile(Constants.DOWNLOAD_URL, tempFolder);
            xmlConvertor.convertXML(tempFolder);
        }finally {
            if(tempFolder!=null) {
                Utility.deleteFolder(tempFolder);
            }
        }
        logger.info("End :: process");

    }
}
