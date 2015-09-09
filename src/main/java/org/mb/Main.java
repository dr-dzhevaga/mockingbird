package org.mb;

import org.mb.binding.HTTPBinding;
import org.mb.cli.CLI;
import org.mb.cli.CommonsCLI;
import org.mb.cli.ParsingException;
import org.mb.http.*;
import org.mb.loader.Loader;
import org.mb.loader.parsing.InputFormat;

import static org.mb.cli.CommonsCLI.*;

/**
 * Created by Dmitriy Dzhevaga on 17.06.2015.
 */
public class Main {
    public static void main(String[] args) throws Exception {

        final CLI cli = CommonsCLI.newInstance();
        try {
            cli.parse(args);
        } catch (ParsingException e) {
            System.out.println(e.getMessage());
            cli.printHelp();
            return;
        }

        final boolean printHelp = cli.hasOption(HELP);
        final int serverPort = Integer.parseInt(cli.getOptionValue(PORT));
        final String filePath = cli.getOptionValue(FILE);
        final InputFormat fileFormat = InputFormat.of(cli.getOptionValue(FORMAT));

        if(printHelp) {
            cli.printHelp();
            System.exit(0);
        }

        final HTTPBinding httpBinding = Loader.loadBinding(filePath, fileFormat);
        final HTTPServer server = JettyHTTPServer.newInstance(serverPort);
        server.setHandler(new Handler() {
            @Override
            public HTTPResponse handle(HTTPRequest request) {
                return httpBinding.resolve(request);
            }
        });
        server.start();
        server.join();
    }
}