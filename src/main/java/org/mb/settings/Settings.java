package org.mb.settings;

import com.google.common.base.Charsets;
import org.mb.http.mapping.HandlerDataMapping;
import org.mb.parsing.Parsing;
import org.mb.settings.marshalling.Marshaller;
import org.mb.settings.marshalling.MarshallingException;
import org.mb.settings.parsing.*;

import java.io.*;

/**
 * Created by Dmitriy Dzhevaga on 12.09.2015.
 */
public class Settings {
    private final HandlerDataMapping mapping;
    private final Parsing parsing;

    public Settings(HandlerDataMapping mapping, Parsing parsing) {
        this.mapping = mapping;
        this.parsing = parsing;
    }

    public static Settings load(String filePath, FileFormat fileFormat) throws IOException, ParsingException, MarshallingException {
        Parser parser = ParserFactory.newParser(fileFormat);
        InputStream is = new FileInputStream(filePath);
        try(Reader r = new InputStreamReader(is, Charsets.UTF_8)) {
            Object o = parser.parse(r);
            return Marshaller.from(o).toSettings();
        }
    }

    public Parsing getParsing() {
        return parsing;
    }

    public HandlerDataMapping getMapping() {
        return mapping;
    }
}