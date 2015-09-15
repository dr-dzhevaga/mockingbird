package org.mb.settings;

import com.google.common.base.Charsets;
import com.google.common.collect.Table;
import org.mb.http.mapping.HandlerDataMapping;
import org.mb.parsing.InputFormat;
import org.mb.settings.marshalling.Marshaller;
import org.mb.settings.marshalling.MarshallingException;
import org.mb.settings.parsing.Parser;
import org.mb.settings.parsing.ParserFactory;
import org.mb.settings.parsing.ParsingException;

import java.io.*;

/**
 * Created by Dmitriy Dzhevaga on 12.09.2015.
 */
public class Settings {
    private final HandlerDataMapping mapping;
    private final Table<InputFormat, String, String> parsing;

    public Settings(HandlerDataMapping mapping, Table<InputFormat, String, String> parsing) {
        this.mapping = mapping;
        this.parsing = parsing;
    }

    public static Settings load(String filePath, org.mb.settings.parsing.InputFormat fileFormat) throws IOException, ParsingException, MarshallingException {
        Parser parser = ParserFactory.newParser(fileFormat);
        InputStream is = new FileInputStream(filePath);
        try(Reader r = new InputStreamReader(is, Charsets.UTF_8)) {
            Object o = parser.parse(r);
            return Marshaller.from(o).toSettings();
        }
    }

    public Table<InputFormat, String, String> getParsing() {
        return parsing;
    }

    public HandlerDataMapping getMapping() {
        return mapping;
    }
}