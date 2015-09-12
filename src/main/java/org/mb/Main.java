package org.mb;

import org.mb.http.MainHandler;
import org.mb.http.basic.HTTPServer;
import org.mb.http.mapping.HTTPRequestResponseMapping;
import org.mb.cli.CLI;
import org.mb.cli.CommonsCLI;
import org.mb.cli.ParsingException;
import org.mb.http.*;
import org.mb.settings.Loader;
import org.mb.settings.parsing.InputFormat;
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

        HTTPRequestResponseMapping mapping = Loader.loadMapping(filePath, fileFormat);
        HTTPServer server = JettyHTTPServer.newInstance(serverPort);
        server.setHandler(new MainHandler(mapping));
        server.start();
    }
}