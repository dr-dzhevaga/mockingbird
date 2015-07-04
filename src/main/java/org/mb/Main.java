package org.mb;

import org.mb.binding.HTTPBinding;
import org.mb.cli.Arguments;
import org.mb.cli.ArgumentsImpl;
import org.mb.http.*;
import org.mb.marshalling.Marshaller;
import org.mb.parsing.InputFormat;
import org.mb.parsing.Parser;
import org.mb.parsing.ParserFactory;
import java.io.FileReader;
import static org.mb.cli.ArgumentsImpl.*;

/**
 * Created by Dmitriy Dzhevaga on 17.06.2015.
 */
public class Main {
    public static void main(String[] args) throws Exception {

        Arguments arguments = ArgumentsImpl.getInstance(args);
        boolean printHelp = arguments.hasOption(HELP_SHORT);
        int serverPort = Integer.valueOf(arguments.getOptionValue(SERVER_PORT_SHORT));
        String bindingsFilePath = arguments.getOptionValue(BINDINGS_PATH_SHORT);
        InputFormat bindingsFileFormat = InputFormat.fromString(arguments.getOptionValue(BINDINGS_FORMAT_SHORT));

        if(printHelp) {
            arguments.printHelp();
            System.exit(0);
        }

        Parser parser = ParserFactory.newParser(bindingsFileFormat);
        Object objectsGraph = parser.parse(new FileReader(bindingsFilePath));
        final HTTPBinding responseResolver = Marshaller.GetAsHTTPBinding(objectsGraph);
        HTTPServer server = HTTPServerImpl.getFactory().create(serverPort);

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