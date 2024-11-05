package com.front;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

public class JrxmlFormattter {
    public static void format() {
        try {
            // Load the JRXML file
            File inputFile = new File("src/main/resources/static/rptrfrepo_detail_shop.jrxml");
            FileInputStream fileInputStream = new FileInputStream(inputFile);
            
            // Parse XML Document
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(fileInputStream);

            // Configure transformer for pretty printing with indentation
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

            // Prepare output file
            File outputFile = new File("src/main/resources/static/rptrfrepo_detail_shop.jrxml");
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            
            // Write the formatted XML to the output file
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(fileOutputStream);
            transformer.transform(source, result);

            // Close streams
            fileInputStream.close();
            fileOutputStream.close();

            System.out.println("JRXML file formatted successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
