package org.mb.parsing;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 11.09.2015.
 */
public class XmlParser extends AbstractParser {
    private Document xml;
    boolean isValid = true;

    public XmlParser(String text) {
        super(text);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            xml = builder.parse(new InputSource(new StringReader(text)));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            isValid = false;
        }
    }

    @Override
    public String parse(String path) throws ParsingException {
        if(isValid) {
            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            try {
                XPathExpression expr = xpath.compile(path);
                return expr.evaluate(xml);
            } catch (XPathExpressionException e) {
                throw new ParsingException(e);
            }
        } else {
            return "";
        }
    }

    @Override
    public Map<String, String> parse(Map<String, String> paths) throws ParsingException {
        Map<String, String> results = new HashMap<>(paths.size());
        if(isValid) {
            for (Map.Entry<String, String> path : paths.entrySet()) {
                results.put(path.getKey(), parse(path.getValue()));
            }
        }
        return results;
    }
}