package org.mb;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.mb.http.MockingbirdHandler;
import org.mb.http.basic.HTTPServer;
import org.mb.http.basic.JettyServer;
import org.mb.monitor.FileMonitor;

import java.io.File;

/**
 * Created by Dmitriy Dzhevaga on 17.06.2015.
 */
public final class Main {
    @Option(name = "-?", aliases = {"-h", "--help"}, usage = "print this message")
    private boolean help;

    @Option(name = "-p", aliases = "--port", usage = "specify server port", metaVar = "number", required = true)
    private int port;

    @Option(name = "-f", aliases = "--file", usage = "specify settings file", metaVar = "path", required = true)
    private File file;

    @Option(name = "-ff", aliases = "--file-format", usage = "specify settings file format", metaVar = "YAML|JSON", required = true)
    private String format;

    @Option(name = "-d", aliases = "--debug", usage = "enable debug mode")
    private boolean debug;

    @Option(name = "-r", aliases = "--reload", usage = "specify settings file reload period", metaVar = "sec")
    private int reload;

    private Main() {
    }

    public static void main(String... args) throws Exception {
        new Main().doMain(args);
    }

    public void doMain(String... args) throws Exception {
        if (!parseArgs(args)) {
            return;
        }
        if (debug) {
            Logger.getLogger("org.mb").setLevel(Level.DEBUG);
        }
        MockingbirdHandler handler = new MockingbirdHandler(file, format);
        if (reload > 0) {
            new FileMonitor(file, handler::reloadSettings).schedule(reload);
        }
        HTTPServer server = new JettyServer(port);
        server.setHandler(handler);
        server.start();
    }

    private boolean parseArgs(String... args) {
        CmdLineParser cmdLineParser = new CmdLineParser(this);
        try {
            cmdLineParser.parseArgument(args);
        } catch (CmdLineException e) {
            if (!help) {
                System.err.println(e.getMessage());
            }
            cmdLineParser.printUsage(System.out);
            return false;
        }
        if (help) {
            cmdLineParser.printUsage(System.out);
        }
        return true;
    }
}