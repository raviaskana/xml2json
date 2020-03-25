package org.developer.xml.convertor;

import org.apache.log4j.Logger;
import org.developer.exception.XMLConversionException;
import org.developer.util.Constants;
import org.json.JSONObject;
import org.json.XML;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

public class XML2JsonConvertor implements XMLConvertor {

    final static Logger logger = Logger.getLogger(XML2JsonConvertor.class);


    @Override
    public void convertXML(String folder) {
        logger.info("Begin :: convertXML");
       File XMLFolder = new File(folder);
        try {
            transformXMLs(XMLFolder);
            convert2JSON(XMLFolder);
        } catch (FileNotFoundException fnfe) {
            logger.fatal(fnfe);
            throw new XMLConversionException("FileNotFoundException while converting XML",fnfe);
        } catch (TransformerException te) {
            logger.fatal(te);
            throw new XMLConversionException("TransformerException while converting XML",te);
        } catch (IOException ioe) {
            logger.fatal(ioe);
            throw new XMLConversionException("IOException while converting XML",ioe);
        }
        logger.info("End :: convertXML");
    }

    private void transformXMLs(File XMLFolder) throws IOException, TransformerException {
        logger.info("Begin :: transformXMLs");
        TransformerFactory transFactory = TransformerFactory.newInstance();
        URL xsltURL = this.getClass().getClassLoader().getResource(Constants.XSLT);
        Source xsltSource = new StreamSource(xsltURL.openStream(),
                xsltURL.toExternalForm());
        Transformer transformer = transFactory.newTransformer(
                xsltSource);
        Arrays.stream(XMLFolder.listFiles()).filter(file -> file.isFile() && file.getName().endsWith("incidents.xml")).forEach(file -> {
            try {
                final InputStream dataStream = new FileInputStream(file);
                OutputStream outputStream = new FileOutputStream(new File(XMLFolder,"transformed"+file.getName()));
                transformer.transform(new StreamSource(dataStream),
                        new StreamResult(outputStream));
            } catch (TransformerConfigurationException tce) {
                logger.fatal(tce);
            } catch (FileNotFoundException fnfe) {
                logger.fatal(fnfe);
            } catch (TransformerException te) {
                logger.fatal(te);
            }
        });
        logger.info("End :: transformXMLs");
    }

    private void convert2JSON(File XMLFolder){
        logger.info("Begin :: convert2JSON");
        Arrays.stream(XMLFolder.listFiles()).filter(file -> file.getName().contains("transformed")).forEach(file -> {
            try {
                String xmlFilePath = file.getAbsolutePath();
                Path xmlPath = Paths.get(xmlFilePath);
                String XML_STRING = Files.lines(xmlPath).collect(Collectors.joining("\n"));
                JSONObject xmlJSONObj = XML.toJSONObject(XML_STRING, false);
                String jsonPrettyPrintString = xmlJSONObj.getJSONObject("loc").toString(Constants.PRETTY_PRINT_INDENT_FACTOR);
                logger.debug("JSON>>>>>>" + jsonPrettyPrintString);
            } catch (IOException ioe) {
                logger.fatal(ioe);
            }
        });
        logger.info("End :: convert2JSON");
    }
}
