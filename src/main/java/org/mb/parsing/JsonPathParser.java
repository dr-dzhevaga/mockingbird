package org.mb.parsing;

import com.jayway.jsonpath.*;
import org.apache.log4j.Logger;

/**
 * Created by Dmitriy Dzhevaga on 15.09.2015.
 */
public class JsonPathParser extends AbstractParser {
    private static final String LOG_PARSING_ERROR = "Text will not be parsed with JSONPathes: %s";
    private static final Logger Log = Logger.getLogger(JsonPathParser.class);

    private ReadContext readContext;

    public JsonPathParser(String text) {
        super(text);
        try {
            Configuration configuration =  Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);
            readContext = JsonPath.parse(text, configuration);
        } catch (InvalidJsonException e) {
            Log.debug(String.format(LOG_PARSING_ERROR, e.toString()));
            textIsParsed = false;
        }
    }

    @Override
    public String parse(String path) throws ParsingException {
        if (textIsParsed) {
            Object result;
            try {
                result = readContext.read(path);
            } catch(InvalidPathException e) {
                Log.error(e);
                throw new ParsingException(e);
            }
            return result != null ? result.toString() : "";
        } else {
            return "";
        }
    }
}