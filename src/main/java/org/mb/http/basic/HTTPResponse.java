package org.mb.http.basic;

import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Dmitriy Dzhevaga on 17.06.2015.
 */
public final class HTTPResponse {
    private final int statusCode;
    private final Map<String, String> headers;
    private final Content content;
    private final ContentWriter contentWriter;

    private HTTPResponse(int statusCode,
                         Map<String, String> headers,
                         Content content,
                         ContentWriter contentWriter) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.content = content;
        this.contentWriter = contentWriter;
    }

    public static Builder builder() {
        return new Builder();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void writeContentTo(OutputStream os) throws Exception {
        try (InputStream is = content.getInputStream()) {
            contentWriter.write(is, os);
        }
    }

    public HTTPResponse setContentWriter(ContentWriter contentWriter) {
        return new HTTPResponse(this.statusCode, this.headers, this.content, contentWriter);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HTTPResponse)) {
            return false;
        }
        HTTPResponse other = (HTTPResponse) obj;
        return Objects.equals(this.statusCode, other.statusCode)
                && Objects.equals(this.headers, other.headers)
                && Objects.equals(this.content, other.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statusCode, headers, content);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("\tStatus code: %s", getStatusCode()));
        if (!getHeaders().isEmpty()) {
            builder.append(String.format("%n\tHeaders: %s", getHeaders()));
        }
        String content = this.content.toString();
        if (!content.isEmpty()) {
            builder.append(String.format("%n\tContent: %s", content));
        }
        return builder.toString();
    }

    public static final class Builder {
        private int statusCode = 200;
        private Map<String, String> headers = Maps.newHashMap();
        private Content content = new Content("");
        private ContentWriter contentWriter = ByteStreams::copy;

        public Builder setStatusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder setStatusCode(String statusCode) {
            this.statusCode = Integer.parseInt(statusCode);
            return this;
        }

        public Builder addHeader(String name, String value) {
            this.headers.put(name, value);
            return this;
        }

        public Builder addHeaders(Map<String, String> parameters) {
            this.headers.putAll(parameters);
            return this;
        }

        public Builder setContent(String content) {
            this.content = new Content(content);
            return this;
        }

        public Builder setContentWriter(ContentWriter contentWriter) {
            this.contentWriter = contentWriter;
            return this;
        }

        public HTTPResponse build() {
            return new HTTPResponse(statusCode, headers, content, contentWriter);
        }
    }
}