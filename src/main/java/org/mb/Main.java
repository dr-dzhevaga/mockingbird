package org.mb;

import com.google.common.base.Charsets;
import org.mb.binding.HTTPBinding;
import org.mb.cli.Arguments;
import org.mb.cli.ArgumentsImpl;
import org.mb.cli.ParsingException;
import org.mb.http.*;
import org.mb.marshalling.Marshaller;
import org.mb.parsing.InputFormat;
import org.mb.parsing.Parser;
import org.mb.parsing.ParserFactory;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;

import static org.mb.cli.ArgumentsImpl.*;

/**
 * Created by Dmitriy Dzhevaga on 17.06.2015.
 */
public class Main {
    public static void main(String[] args) throws Exception {

        final Arguments arguments = ArgumentsImpl.getInstance();
        try {
            arguments.parse(args);
        } catch (ParsingException e) {
            System.out.println(e.getMessage());
            arguments.printHelp();
            System.exit(0);
            throw e;
        }

        final boolean printHelp = arguments.hasOption(HELP_SHORT);
        final int serverPort = Integer.parseInt(arguments.getOptionValue(SERVER_PORT_SHORT));
        final String bindingsFilePath = arguments.getOptionValue(BINDINGS_PATH_SHORT);
        final InputFormat bindingsFileFormat = InputFormat.fromString(arguments.getOptionValue(BINDINGS_FORMAT_SHORT));

        if(printHelp) {
            arguments.printHelp();
            System.exit(0);
        }

        final Parser parser = ParserFactory.newParser(bindingsFileFormat);
        final Object objectsGraph = parser.parse(new InputStreamReader(new FileInputStream(bindingsFilePath), Charsets.UTF_8));

        final HTTPBinding responseResolver = Marshaller.getAsHTTPBinding(objectsGraph);

        final HTTPServer server = HTTPServerImpl.getFactory().create(serverPort);
        server.setHandler(new Handler() {
            @Override
            public HTTPResponse handle(HTTPRequest request) {
                return responseResolver.resolve(request);
            }
        });
        server.start();
        server.join();
    }
}