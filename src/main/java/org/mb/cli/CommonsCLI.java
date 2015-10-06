package org.mb.cli;

import com.google.common.base.Joiner;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.mb.settings.parsing.FileFormat;

/**
 * Created by Dmitriy Dzhevaga on 04.07.2015.
 */
public final class CommonsCLI implements CLI {
    private static final String COMMAND_LINE_SYNTAX     = "java mockingbird.jar";

    public static final String HELP = "?";
    public static final String PORT = "p";
    public static final String FILE = "f";
    public static final String FORMAT = "ff";
    public static final String DEBUG = "d";
    private static final String FORMAT_ARGUMENTS = Joiner.on('|').join(FileFormat.values());

    private final InitialOption[] initialOptions = {
        new InitialOption(HELP,   "help",        "print this message",           "",               false),
        new InitialOption(PORT,   "port",        "specify server port",          "port",           true),
        new InitialOption(FILE,   "file",        "specify settings file",        "file",           true),
        new InitialOption(FORMAT, "file-format", "specify settings file format", FORMAT_ARGUMENTS, true),
        new InitialOption(DEBUG,  "debug",       "enable debug mode",            "",               false)
    };

    private final Options options = new Options();
    private CommandLine commandLine;
    private boolean isParsed;

    public static CLI newInstance() {
        return new CommonsCLI();
    }

    private CommonsCLI() {
        for (InitialOption initialOption : initialOptions) {
            Option.Builder builder = Option.builder(initialOption.name);
            builder.longOpt(initialOption.longName);
            builder.desc(initialOption.description);
            builder.required(initialOption.isRequired);
            if (!initialOption.argumentName.isEmpty()) {
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
        if (!isParsed) {
            throw new IllegalStateException();
        }
        return commandLine.hasOption(opt);
    }

    @Override
    public String getOptionValue(String opt) {
        if (!isParsed) {
            throw new IllegalStateException();
        }
        return commandLine.getOptionValue(opt);
    }

    @Override
    public void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(COMMAND_LINE_SYNTAX, options, true);
    }

    private static final class InitialOption {
        public final String name;
        public final String longName;
        public final String description;
        public final String argumentName;
        public final boolean isRequired;

        private InitialOption(String name, String longName, String description, String argumentName, boolean isRequired) {
            this.name = name;
            this.longName = longName;
            this.description = description;
            this.argumentName = argumentName;
            this.isRequired = isRequired;
        }
    }
}
