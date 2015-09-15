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

/**
 * Created by Dmitriy Dzhevaga on 11.09.2015.
 */
public class XpathParser extends AbstractParser {
    private Document document;

    public XpathParser(String text) {
        super(text);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(new InputSource(new StringReader(text)));
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
                return expr.evaluate(document);
            } catch (XPathExpressionException e) {
                throw new ParsingException(e);
            }
        } else {
            return "";
        }
    }
}