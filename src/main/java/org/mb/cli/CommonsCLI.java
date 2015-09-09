package org.mb.cli;

import org.apache.commons.cli.*;

/**
 * Created by Dmitriy Dzhevaga on 04.07.2015.
 */
public class CommonsCLI implements CLI {

    private static final String COMMAND_LINE_SYNTAX     = "java mockingbird.jar";

    public static final String HELP = "?";
    public static final String PORT = "p";
    public static final String FILE = "f";
    public static final String FORMAT = "ff";

    InitialOption[] initialOptions = {
        new InitialOption(HELP,   "help",                 "print this message",           "",          false),
        new InitialOption(PORT,   "server-port",          "specify server port",          "port",      true),
        new InitialOption(FILE,   "bindings-file",        "specify bindings file",        "file",      true),
        new InitialOption(FORMAT, "bindings-file-format", "specify bindings file format", "YAML|JSON", true)
    };

    private final Options options = new Options();
    private CommandLine commandLine;
    private boolean isParsed;

    public static CLI newInstance() {
        return new CommonsCLI();
    }

    public CommonsCLI() {
        for(InitialOption initialOption : initialOptions) {
            Option.Builder builder = Option.builder(initialOption.name);
            builder.longOpt(initialOption.longName);
            builder.desc(initialOption.description);
            builder.required(initialOption.isRequired);
            if(!initialOption.argumentName.isEmpty()) {
                builder.hasArg(true);
                builder.argName(initialOption.argumentName);
            }
            options.addOption(builder.build());
        }
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

    static private class InitialOption {
        final String name;
        final String longName;
        final String description;
        final String argumentName;
        final boolean isRequired;

        private InitialOption(String name, String longName, String description, String argumentName, boolean isRequired) {
            this.name = name;
            this.longName = longName;
            this.description = description;
            this.argumentName = argumentName;
            this.isRequired = isRequired;
        }
    }
}