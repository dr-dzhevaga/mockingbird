package org.mb.parsing;

import com.google.common.base.Strings;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;

import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 15.09.2015.
 */
public class Parsing {
    private final Table<PathType, String, String> parsing = HashBasedTable.create();

    public void addParsing(PathType pathType, Map<String, String> paths) {
        for (Map.Entry<String, String> path : paths.entrySet()) {
            parsing.put(pathType, path.getKey(), path.getValue());
        }
    }

    public Map<String, String> parse(String text) throws ParsingException {
        Map<String, String> parsingResult = Maps.newHashMap();
        if(!Strings.isNullOrEmpty(text)) {
            for(PathType pathType : parsing.rowKeySet()) {
                Parser parser = ParserFactory.newParser(pathType, text);
                Map<String, String> paths = parsing.row(pathType);
                parsingResult.putAll(parser.parse(paths));
            }
        }
        return parsingResult;
    }

    @Override
    public String toString() {
        return parsing.toString();
    }
}
