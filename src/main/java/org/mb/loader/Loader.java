package org.mb.loader;

import com.google.common.base.Charsets;
import org.mb.binding.HTTPBinding;
import org.mb.loader.marshalling.Marshaller;
import org.mb.loader.marshalling.MarshallingException;
import org.mb.loader.parsing.InputFormat;
import org.mb.loader.parsing.Parser;
import org.mb.loader.parsing.ParserFactory;
import org.mb.loader.parsing.ParsingException;
import java.io.*;

/**
 * Created by Dmitriy Dzhevaga on 09.09.2015.
 */
public class Loader {
    public static HTTPBinding loadBinding(String filePath, InputFormat fileFormat) throws IOException, ParsingException, MarshallingException {
        Parser parser = ParserFactory.newParser(fileFormat);
        InputStream is = new FileInputStream(filePath);
        try(Reader r = new InputStreamReader(is, Charsets.UTF_8)) {
            Object objectsGraph = parser.parse(r);
            return Marshaller.toHTTPBinding(objectsGraph);
        }
    }
}