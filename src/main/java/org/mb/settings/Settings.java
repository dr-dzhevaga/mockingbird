package org.mb.settings;

import com.google.common.base.Charsets;
import org.apache.log4j.Logger;
import org.mb.http.mapping.ResponseDataMapping;
import org.mb.parsing.Parsing;
import org.mb.settings.marshalling.Marshaller;
import org.mb.settings.marshalling.MarshallingException;
import org.mb.settings.parsing.*;

import java.io.*;

/**
 * Created by Dmitriy Dzhevaga on 12.09.2015.
 */
public class Settings {
    private static final String LOG_GLOBAL_PARSING = "Global parsing:\n%s";

    private final ResponseDataMapping mapping;
    private final Parsing parsing;

    public Settings(ResponseDataMapping mapping, Parsing parsing) {
        this.mapping = mapping;
        this.parsing = parsing;
    }

    public static Settings load(String filePath, FileFormat fileFormat) throws IOException, ParsingException, MarshallingException {
        Parser parser = ParserFactory.newParser(fileFormat);
        InputStream is = new FileInputStream(filePath);
        try(Reader r = new InputStreamReader(is, Charsets.UTF_8)) {
            Object o = parser.parse(r);
            Settings settings = Marshaller.from(o).toSettings();
            Logger.getLogger(Settings.class).info(String.format(LOG_GLOBAL_PARSING, settings.getParsing()));
            return settings;
        }
    }

    public Parsing getParsing() {
        return parsing;
    }

    public ResponseDataMapping getMapping() {
        return mapping;
    }
}