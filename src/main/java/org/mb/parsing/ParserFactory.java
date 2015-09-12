package org.mb.parsing;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 11.09.2015.
 */
public class ParserFactory {
    private static final Map<ParserType, Parser> parsers = Maps.newHashMap();

    static {
        // TODO: load all Parser implementations dynamically
        parsers.put(ParserType.DUMMY, new DummyParser());
    }

    public static Parser getParser(ParserType parserType) throws IllegalArgumentException {
        if(!parsers.containsKey(parserType)) {
            throw new IllegalArgumentException("Unsupported parser type: " + parserType.toString());
        }
        return parsers.get(parserType);
    }
}