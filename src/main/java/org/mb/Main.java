package org.mb;

import org.mb.http.mapping.HTTPRequestResponseMapping;
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

        CLI cli = CommonsCLI.newInstance();
        try {
            cli.parse(args);
        } catch (ParsingException e) {
            System.out.println(e.getMessage());
            cli.printHelp();
            return;
        }

        boolean printHelp = cli.hasOption(HELP);
        int serverPort = Integer.parseInt(cli.getOptionValue(PORT));
        String filePath = cli.getOptionValue(FILE);
        InputFormat fileFormat = InputFormat.of(cli.getOptionValue(FORMAT));

        if(printHelp) {
            cli.printHelp();
            return;
        }

        final HTTPRequestResponseMapping httpRequestResponseMapping = Loader.loadMapping(filePath, fileFormat);
        HTTPServer server = JettyHTTPServer.newInstance(serverPort);
        server.setHandler(new Handler() {
            @Override
            public HTTPResponse handle(HTTPRequest request) {
                return httpRequestResponseMapping.findResponse(request);
            }
        });
        server.start();
    }
}