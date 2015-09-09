package org.mb.cli;

/**
 * Created by Dmitriy Dzhevaga on 04.07.2015.
 */
public interface CLI {
    public void parse(String[] args) throws ParsingException;
    public boolean hasOption(String opt);
    public String getOptionValue(String opt);
    public void printHelp();
}