package org.mb.scripting;

import java.io.Reader;
import java.io.Writer;

/**
 * Created by Dmitriy Dzhevaga on 16.09.2015.
 */
public interface Engine {
    void put(String name, String value);
    void eval(Reader reader);
}