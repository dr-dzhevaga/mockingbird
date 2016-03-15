package org.mb.settings;

import com.google.common.base.Charsets;
import org.mb.http.mapping.Mapping;
import org.mb.parsing.Parsing;
import org.mb.settings.marshalling.Marshaller;
import org.mb.settings.marshalling.MarshallingException;
import org.mb.settings.parsing.FileFormat;
import org.mb.settings.parsing.Parser;
import org.mb.settings.parsing.ParserFactory;
import org.mb.settings.parsing.ParsingException;

import java.io.*;

/**
 * Created by Dmitriy Dzhevaga on 12.09.2015.
 */
public final class Settings {
    private final Mapping mapping;
    private final Parsing parsing;

    public Settings(Mapping mapping, Parsing parsing) {
        this.mapping = mapping;
        this.parsing = parsing;
    }

    public static Settings load(File file, String format) throws ParsingException, MarshallingException, IOException {
        Parser parser = ParserFactory.newParser(FileFormat.of(format));
        InputStream is = new FileInputStream(file);
        try (Reader r = new InputStreamReader(is, Charsets.UTF_8)) {
            Object o = parser.parse(r);
            return Marshaller.from(o).toSettings();
        }
    }

    public Parsing getParsing() {
        return parsing;
    }

    public Mapping getMapping() {
        return mapping;
    }
}