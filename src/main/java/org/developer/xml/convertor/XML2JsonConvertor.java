package org.developer.xml.convertor;

import org.developer.exception.XMLConversionException;
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

    public static final int PRETTY_PRINT_INDENT_FACTOR = 4;

    @Override
    public void convertXML(String folder) {
       File XMLFolder = new File(folder);
        try {
            transformXMLs(XMLFolder);
            convert2JSON(XMLFolder);
        } catch (FileNotFoundException e) {
            throw new XMLConversionException("FileNotFoundException while converting XML",e);
        } catch (TransformerException e) {
            throw new XMLConversionException("TransformerException while converting XML",e);
        } catch (IOException e) {
            throw new XMLConversionException("IOException while converting XML",e);
        }
    }

    private void transformXMLs(File XMLFolder) throws IOException, TransformerException {
        TransformerFactory transFactory = TransformerFactory.newInstance();
        URL xsltURL = this.getClass().getClassLoader().getResource("incidents.xsl");
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
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            }
        });
    }

    private void convert2JSON(File XMLFolder){
        Arrays.stream(XMLFolder.listFiles()).filter(file -> file.getName().contains("transformed")).forEach(file -> {
            try {
                String xmlFilePath = file.getAbsolutePath();
                Path xmlPath = Paths.get(xmlFilePath);
                String XML_STRING = Files.lines(xmlPath).collect(Collectors.joining("\n"));
                JSONObject xmlJSONObj = XML.toJSONObject(XML_STRING, false);
                String jsonPrettyPrintString = xmlJSONObj.getJSONObject("loc").toString(PRETTY_PRINT_INDENT_FACTOR);
                System.out.println("PRINTING STRING :::::::::::::::::::::" + jsonPrettyPrintString);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
