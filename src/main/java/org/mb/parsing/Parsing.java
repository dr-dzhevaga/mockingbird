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
    private static final Logger log = Logger.getLogger(Parsing.class);
    private final Table<PathType, String, String> paths = HashBasedTable.create();

    public void addPaths(PathType pathType, Map<String, String> paths) {
        for (Map.Entry<String, String> path : paths.entrySet()) {
            this.paths.put(pathType, path.getKey(), path.getValue());
        }
    }

    public Map<String, String> parse(String text) throws ParsingException {
        Map<String, String> parsed = Maps.newHashMap();
        if (!Strings.isNullOrEmpty(text)) {
            for (PathType pathType : paths.rowKeySet()) {
                Parser parser = ParserFactory.newParser(pathType, text);
                parsed.putAll(parser.parse(paths.row(pathType)));
                log.debug(String.format("%s parsing result: %s", pathType, parsed));
            }
        }
        return parsed;
    }

    @Override
    public String toString() {
        return paths.toString();
    }
}
