package org.mb.parsing;

import com.jayway.jsonpath.*;

/**
 * Created by Dmitriy Dzhevaga on 15.09.2015.
 */
public class JsonPathParser extends AbstractParser {
    private ReadContext readContext;

    public JsonPathParser(String text) {
        super(text);
        try {
            Configuration configuration =  Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);
            readContext = JsonPath.parse(text, configuration);
        } catch (InvalidJsonException e) {
            isValid = false;
        }
    }

    @Override
    public String parse(String path) throws ParsingException {
        if(isValid) {
            Object result;
            try {
                result = readContext.read(path);

            } catch(InvalidPathException e) {
                throw new ParsingException(e);
            }
            return result != null ? result.toString() : "";
        } else {
            return "";
        }
    }
}