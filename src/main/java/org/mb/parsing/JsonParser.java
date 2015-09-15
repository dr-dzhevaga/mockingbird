package org.mb.parsing;

import com.google.common.collect.Maps;
import com.jayway.jsonpath.*;

import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 15.09.2015.
 */
public class JsonParser extends AbstractParser {
    private ReadContext readContext;
    private boolean isValid = true;

    public JsonParser(String text) {
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
            Object object = readContext.read(path);
            return object != null ? object.toString() : "";
        } else {
            return "";
        }
    }

    @Override
    public Map<String, String> parse(Map<String, String> paths) throws ParsingException {
        if(isValid) {
            return super.parse(paths);
        } else {
            return Maps.newHashMap();
        }
    }
}