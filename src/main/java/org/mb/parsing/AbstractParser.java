package org.mb.parsing;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 14.09.2015.
 */
public abstract class AbstractParser implements Parser {
    protected final String text;
    protected boolean isValid = true;

    public AbstractParser(String text) {
        this.text = text;
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
