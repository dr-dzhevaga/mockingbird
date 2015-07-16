package org.mb.cli;

import com.google.common.base.Strings;
import org.apache.commons.cli.*;

/**
 * Created by Dmitriy Dzhevaga on 04.07.2015.
 */
public class ArgumentsImpl implements Arguments {

    public  static final String HELP_SHORT              = "?";
    private static final String HELP_LONG               = "help";
    private static final String HELP_DESC               = "print this message";

    public  static final String SERVER_PORT_SHORT       = "p";
    private static final String SERVER_PORT_LONG        = "server-port";
    private static final String SERVER_PORT_ARG         = "port";
    private static final String SERVER_PORT_DESC        = "specify server port";

    public  static final String BINDINGS_PATH_SHORT     = "f";
    private static final String BINDINGS_PATH_LONG      = "bindings-file";
    private static final String BINDINGS_PATH_ARG       = "file";
    private static final String BINDINGS_PATH_DESC      = "specify bindings file";

    public  static final String BINDINGS_FORMAT_SHORT   = "ff";
    private static final String BINDINGS_FORMAT_LONG    = "bindings-file-format";
    private static final String BINDINGS_FORMAT_ARG     = "YAML|JSON";
    private static final String BINDINGS_FORMAT_DESC    = "specify bindings file format";

    private static final String COMMAND_LINE_SYNTAX     = "java mockingbird.jar";

    private final Options options = new Options();
    private CommandLine commandLine;
    private boolean isParsed;

    public static Arguments getInstance() {
        return new ArgumentsImpl();
    }

    public ArgumentsImpl() {
        addOption(HELP_SHORT, HELP_LONG, HELP_DESC, false, "");
        addOption(SERVER_PORT_SHORT, SERVER_PORT_LONG, SERVER_PORT_DESC, true, SERVER_PORT_ARG);
        addOption(BINDINGS_PATH_SHORT, BINDINGS_PATH_LONG, BINDINGS_PATH_DESC, true, BINDINGS_PATH_ARG);
        addOption(BINDINGS_FORMAT_SHORT, BINDINGS_FORMAT_LONG, BINDINGS_FORMAT_DESC, true, BINDINGS_FORMAT_ARG);
    }

    private void addOption(String optShort, String optLong, String desc, boolean required, String arg) {
        Option.Builder builder = Option.builder(optShort);
        if(!Strings.isNullOrEmpty(optLong)) builder.longOpt(optLong);
        if(!Strings.isNullOrEmpty(desc)) builder.desc(desc);
        builder.required(required);
        if(!Strings.isNullOrEmpty(arg)) {
            builder.hasArg(true);
            builder.argName(arg);
        }
        options.addOption(builder.build());
    }

    @Override
    public void parse(String[] args) throws ParsingException {
        CommandLineParser parser = new DefaultParser();
        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            throw new ParsingException(e);
        }
        isParsed = true;
    }

    @Override
    public boolean hasOption(String opt) {
        if(!isParsed) throw new IllegalStateException();
        return commandLine.hasOption(opt);
    }

    @Override
    public String getOptionValue(String opt) {
        if(!isParsed) throw new IllegalStateException();
        return commandLine.getOptionValue(opt);
    }

    @Override
    public void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(COMMAND_LINE_SYNTAX, options, true);
    }
}