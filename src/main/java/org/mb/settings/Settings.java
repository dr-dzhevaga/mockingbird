package org.mb.settings;

import com.google.common.base.Charsets;
import com.google.common.collect.Table;
import org.mb.http.mapping.HandlerDataMapping;
import org.mb.parsing.ParserType;
import org.mb.settings.marshalling.Marshaller;
import org.mb.settings.marshalling.MarshallingException;
import org.mb.settings.parsing.InputFormat;
import org.mb.settings.parsing.Parser;
import org.mb.settings.parsing.ParserFactory;
import org.mb.settings.parsing.ParsingException;

import java.io.*;

/**
 * Created by Dmitriy Dzhevaga on 12.09.2015.
 */
public class Settings {
    public final HandlerDataMapping mapping;
    public final Table<ParserType, String, String> parsing;

    public Settings(HandlerDataMapping mapping, Table<ParserType, String, String> parsing) {
        this.mapping = mapping;
        this.parsing = parsing;
    }

    public static Settings load(String filePath, InputFormat fileFormat) throws IOException, ParsingException, MarshallingException {
        Parser parser = ParserFactory.newParser(fileFormat);
        InputStream is = new FileInputStream(filePath);
        try(Reader r = new InputStreamReader(is, Charsets.UTF_8)) {
            Object objectsGraph = parser.parse(r);
            return Marshaller.toSettings(objectsGraph);
        }
    }
}