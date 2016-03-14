package org.mb.http.basic;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Dmitriy Dzhevaga on 14.03.2016.
 */
@FunctionalInterface
public interface ContentWriter {
    void write(InputStream is, OutputStream os) throws Exception;
}
