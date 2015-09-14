package org.mb.parsing;

/**
 * Created by Dmitriy Dzhevaga on 14.09.2015.
 */
public abstract class AbstractParser implements Parser {
    protected final String text;

    public AbstractParser(String text) {
        this.text = text;
    }
}
