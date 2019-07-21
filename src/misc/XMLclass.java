/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package misc;

/**
 *
 * @author root
 */
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.LogManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XMLclass implements ErrorHandler {
    
    static Logger log = LogManager.getLogger(XMLclass.class.getName());

    public static void main(String args[]) throws Exception {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(true);
        builderFactory.setValidating(true);

        DocumentBuilder builder = null;
        builder = builderFactory.newDocumentBuilder();
        builder.setErrorHandler(new XMLclass());
        Document xmlDoc = null;

        xmlDoc = builder.parse(new File("C://JTerminals/init.xml"));
        DocumentType doctype = xmlDoc.getDoctype();
        log.info("DOCTYPE node:\n" + doctype);
        listNodes(xmlDoc.getDocumentElement(), "");
    }

    static void listNodes(Node node, String indent) {
        String nodeName = node.getNodeName();
        log.info(indent + nodeName + " Node, type is "
                + node.getClass().getName() + ":");
        log.info(indent + " " + node);

        NodeList list = node.getChildNodes();
        if (list.getLength() > 0) {
            log.info(indent + "Child Nodes of " + nodeName + " are:");
            for (int i = 0; i < list.getLength(); i++) {
                listNodes(list.item(i), indent + " ");
            }
        }
    }

    public void fatalError(SAXParseException spe) throws SAXException {
        log.info("Fatal error at line " + spe.getLineNumber());
        log.info(spe.getMessage());
        throw spe;
    }

    public void warning(SAXParseException spe) {
        log.info("Warning at line " + spe.getLineNumber());
        log.info(spe.getMessage());
    }

    public void error(SAXParseException spe) {
        log.info("Error at line " + spe.getLineNumber());
        log.info(spe.getMessage());
    }
}
