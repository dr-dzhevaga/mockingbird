package org.mb.http.basic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Dmitriy Dzhevaga on 17.09.2015.
 */
public interface Content {
    void writeTo(OutputStream outputStream) throws IOException;
    InputStream getStream();
}