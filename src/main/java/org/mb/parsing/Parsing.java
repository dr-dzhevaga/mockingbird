package org.mb.parsing;

import com.google.common.base.Strings;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 15.09.2015.
 */
public final class Parsing {
    private static final String LOG_PARSING_RESULT = "%s parsing result: %s";
    private static final Logger LOG = Logger.getLogger(Parsing.class);

    private final Table<PathType, String, String> parsing = HashBasedTable.create();

    public void addParsing(final PathType pathType, final Map<String, String> paths) {
        for (Map.Entry<String, String> path : paths.entrySet()) {
            parsing.put(pathType, path.getKey(), path.getValue());
        }
    }

    public Map<String, String> parse(final String text) throws ParsingException {
        Map<String, String> parsingResult = Maps.newHashMap();
        if (!Strings.isNullOrEmpty(text)) {
            for (PathType pathType : parsing.rowKeySet()) {
                Parser parser = ParserFactory.newParser(pathType, text);
                Map<String, String> paths = parsing.row(pathType);
                parsingResult.putAll(parser.parse(paths));
                LOG.debug(String.format(LOG_PARSING_RESULT, pathType, parsingResult));
            }
        }
        return parsingResult;
    }

    @Override
    public String toString() {
        return parsing.toString();
    }
}
