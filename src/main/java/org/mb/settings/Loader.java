package org.mb.settings;

import com.google.common.base.Charsets;
import org.mb.http.mapping.HTTPRequestResponseMapping;
import org.mb.settings.marshalling.Marshaller;
import org.mb.settings.marshalling.MarshallingException;
import org.mb.settings.parsing.InputFormat;
import org.mb.settings.parsing.Parser;
import org.mb.settings.parsing.ParserFactory;
import org.mb.settings.parsing.ParsingException;
import java.io.*;

/**
 * Created by Dmitriy Dzhevaga on 09.09.2015.
 */
public class Loader {
    public static HTTPRequestResponseMapping loadMapping(String filePath, InputFormat fileFormat) throws IOException, ParsingException, MarshallingException {
        Parser parser = ParserFactory.newParser(fileFormat);
        InputStream is = new FileInputStream(filePath);
        try(Reader r = new InputStreamReader(is, Charsets.UTF_8)) {
            Object objectsGraph = parser.parse(r);
            return Marshaller.toHTTPMapping(objectsGraph);
        }
    }
}