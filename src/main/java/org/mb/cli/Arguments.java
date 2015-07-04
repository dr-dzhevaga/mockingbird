package org.mb.cli;

/**
 * Created by Dmitriy Dzhevaga on 04.07.2015.
 */
public interface Arguments {
    public boolean hasOption(String opt);
    public String getOptionValue(String opt);
    public void printHelp();
}