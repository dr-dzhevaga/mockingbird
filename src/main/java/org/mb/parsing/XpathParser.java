package org.mb.parsing;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

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
public final class XpathParser extends AbstractParser {
    private static final String LOG_PARSING_ERROR = "Text will not be parsed with Xpathes: %s";
    private static final Logger LOG = Logger.getLogger(XpathParser.class);

    private static final ErrorHandler QUIET_ERROR_HANDLER = new QuietErrorHandler();
    private final XPathFactory xpathFactory = XPathFactory.newInstance();
    private Document document;

    public XpathParser(final String text) {
        super(text);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(QUIET_ERROR_HANDLER);
            document = builder.parse(new InputSource(new StringReader(text)));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            LOG.debug(String.format(LOG_PARSING_ERROR, e.toString()));
            textIsParsed = false;
        }
    }

    @Override
    public String parse(final String path) throws ParsingException {
        if (textIsParsed) {
            XPath xpath = xpathFactory.newXPath();
            try {
                XPathExpression expr = xpath.compile(path);
                return expr.evaluate(document);
            } catch (XPathExpressionException e) {
                LOG.error(e);
                throw new ParsingException(e);
            }
        } else {
            return "";
        }
    }

    private static class QuietErrorHandler implements ErrorHandler {
        @Override
        public void warning(final SAXParseException exception) throws SAXException { }
        @Override
        public void error(final SAXParseException exception) throws SAXException { }
        @Override
        public void fatalError(final SAXParseException exception) throws SAXException { }
    }
}