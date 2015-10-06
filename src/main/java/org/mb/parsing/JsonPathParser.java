package org.mb.parsing;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.ReadContext;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.InvalidPathException;
import org.apache.log4j.Logger;

/**
 * Created by Dmitriy Dzhevaga on 15.09.2015.
 */
public final class JsonPathParser extends AbstractParser {
    private static final String LOG_PARSING_ERROR = "Text will not be parsed with JSONPathes: %s";
    private static final Logger LOG = Logger.getLogger(JsonPathParser.class);

    private ReadContext readContext;

    public JsonPathParser(final String text) {
        super(text);
        try {
            Configuration configuration =  Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);
            readContext = JsonPath.parse(text, configuration);
        } catch (InvalidJsonException e) {
            LOG.debug(String.format(LOG_PARSING_ERROR, e.toString()));
            textIsParsed = false;
        }
    }

    @Override
    public String parse(final String path) throws ParsingException {
        if (textIsParsed) {
            Object result;
            try {
                result = readContext.read(path);
            } catch (InvalidPathException e) {
                LOG.error(e);
                throw new ParsingException(e);
            }
            if (result != null) {
                return result.toString();
            }
        }
        return "";
    }
}