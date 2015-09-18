package org.mb.http.basic;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;

import java.io.*;

/**
 * Created by Dmitriy Dzhevaga on 17.09.2015.
 */
public class DefaultContent implements Content {
    private final static int TO_STRING_MAX_LENGTH = 1024;
    private final String source;
    private final boolean sourceIsFilePath;

    public DefaultContent(String source) {
        this.source = source;
        sourceIsFilePath = new File(source).exists();
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        try(InputStream inputStream = getStream()) {
            ByteStreams.copy(inputStream, outputStream);
        }
    }

    @Override
    public InputStream getStream() {
        if(sourceIsFilePath) {
            try {
                return new FileInputStream(source);
            } catch (FileNotFoundException e) {
                return new ByteArrayInputStream(e.getMessage().getBytes(Charsets.UTF_8));
            }
        } else {
            return new ByteArrayInputStream(source.getBytes(Charsets.UTF_8));
        }
    }

    private String toString(int maxLength) {
        try(InputStream stream = getStream()) {
            return CharStreams.toString(new InputStreamReader(ByteStreams.limit(stream, maxLength), Charsets.UTF_8));
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    public String toString() {
        return toString(TO_STRING_MAX_LENGTH);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(!(obj instanceof DefaultContent)) {
            return false;
        }
        DefaultContent content = (DefaultContent)obj;
        if(this.source != content.source) {
            return false;
        }
        return true;
    }
}