package org.mb.settings;

import com.google.common.base.Charsets;
import org.mb.http.mapping.Mapping;
import org.mb.parsing.Parsing;
import org.mb.settings.marshalling.Marshaller;
import org.mb.settings.marshalling.MarshallingException;
import org.mb.settings.parsing.FileFormat;
import org.mb.settings.parsing.ParsingException;
import org.mb.settings.parsing.ParserFactory;
import org.mb.settings.parsing.Parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.Reader;
import java.io.InputStreamReader;

/**
 * Created by Dmitriy Dzhevaga on 12.09.2015.
 */
public final class Settings {
    private final Mapping mapping;
    private final Parsing parsing;

    public Settings(final Mapping mapping, final Parsing parsing) {
        this.mapping = mapping;
        this.parsing = parsing;
    }

    public static Settings load(final String filePath, final FileFormat fileFormat) throws IOException, ParsingException, MarshallingException {
        Parser parser = ParserFactory.newParser(fileFormat);
        InputStream is = new FileInputStream(filePath);
        try (Reader r = new InputStreamReader(is, Charsets.UTF_8)) {
            Object o = parser.parse(r);
            Settings settings = Marshaller.from(o).toSettings();
            return settings;
        }
    }

    public Parsing getParsing() {
        return parsing;
    }

    public Mapping getMapping() {
        return mapping;
    }
}