package org.mb.http.basic;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;

import java.io.*;
import java.util.Objects;

/**
 * Created by Dmitriy Dzhevaga on 17.09.2015.
 */
public final class Content {
    private static final int TO_STRING_LENGTH_LIMIT = 1024;
    private final String source;
    private final boolean sourceIsFilePath;

    public Content(String source) {
        this.source = source;
        sourceIsFilePath = new File(source).exists();
    }

    public InputStream getInputStream() {
        if (sourceIsFilePath) {
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
        try (InputStream stream = getInputStream()) {
            return CharStreams.toString(new InputStreamReader(ByteStreams.limit(stream, maxLength), Charsets.UTF_8));
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    public String toString() {
        return toString(TO_STRING_LENGTH_LIMIT);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Content)) {
            return false;
        }
        final Content other = (Content) obj;
        return  Objects.equals(this.source, other.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source);
    }
}