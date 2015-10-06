package org.mb.cli;

/**
 * Created by Dmitriy Dzhevaga on 04.07.2015.
 */
public interface CLI {
    void parse(String[] args) throws ParsingException;
    boolean hasOption(String opt);
    String getOptionValue(String opt);
    void printHelp();
}
