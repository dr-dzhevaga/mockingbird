package org.mb.parsing;

import com.jayway.jsonpath.*;
import org.apache.log4j.Logger;

/**
 * Created by Dmitriy Dzhevaga on 15.09.2015.
 */
public final class JsonPathParser extends AbstractParser {
    private static final Logger log = Logger.getLogger(JsonPathParser.class);

    private ReadContext readContext;

    public JsonPathParser(String text) {
        super(text);
        try {
            Configuration configuration =  Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);
            readContext = JsonPath.parse(text, configuration);
        } catch (InvalidJsonException e) {
            log.debug(String.format("Text will not be parsed with JSONPathes: %s", e.toString()));
            textIsParsed = false;
        }
    }

    @Override
    public String parse(String path) throws ParsingException {
        if (textIsParsed) {
            Object result;
            try {
                result = readContext.read(path);
            } catch (InvalidPathException e) {
                log.error(e);
                throw new ParsingException(e);
            }
            if (result != null) {
                return result.toString();
            }
        }
        return "";
    }
}