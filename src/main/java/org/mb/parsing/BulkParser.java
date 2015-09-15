package org.mb.parsing;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import org.mb.parsing.ParserFactory;
import org.mb.parsing.ParsingException;
import org.mb.parsing.Parser;
import org.mb.parsing.PathType;

import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 15.09.2015.
 */
public class BulkParser {
    private final Table<PathType, String, String> paths = HashBasedTable.create();

    public void add(PathType pathType, Map<String, String> paths) {
        for (Map.Entry<String, String> path : paths.entrySet()) {
            this.paths.put(pathType, path.getKey(), path.getValue());
        }
    }

    public Map<String, String> parse(String text) throws ParsingException {
        Map<String, String> parsingResults = Maps.newHashMap();
        for(PathType pathType : this.paths.rowKeySet()) {
            Parser parser = ParserFactory.newParser(pathType, text);
            Map<String, String> paths = this.paths.row(pathType);
            parsingResults.putAll(parser.parse(paths));
        }
        return Maps.newHashMap();
    }
}
