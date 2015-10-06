package org.mb.http.basic;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Dmitriy Dzhevaga on 17.09.2015.
 */
public interface Content {
    void writeTo(OutputStream outputStream) throws Exception;
    InputStream getStream();
}