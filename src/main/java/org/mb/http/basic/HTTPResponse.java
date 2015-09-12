package org.mb.http.basic;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import java.io.*;
import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 17.06.2015.
 */
public class HTTPResponse {
    private static final int DEFAULT_STATUS_CODE    = 200;
    private static final int LOG_MAX_CONTENT_LENGTH = 1024;

    final private int statusCode;
    final private Map<String, String> headers;
    final private String content;
    final private boolean contentIsFilePath;

    private HTTPResponse(int statusCode, Map<String, String> headers, String content, boolean contentIsFilePath) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.content = content;
        this.contentIsFilePath = contentIsFilePath;
    }

    public static Builder newBuilder() {return new Builder();}

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public InputStream getContent() {
        if(contentIsFilePath) {
            try {
                return new FileInputStream(content);
            } catch (FileNotFoundException e) {
                return new ByteArrayInputStream(e.getMessage().getBytes(Charsets.UTF_8));
            }
        } else {
            return new ByteArrayInputStream(content.getBytes(Charsets.UTF_8));
        }
    }

    private String getContentAsString(int maxLength) {
        try(InputStream stream = getContent()) {
            return CharStreams.toString(new InputStreamReader(ByteStreams.limit(stream, maxLength), Charsets.UTF_8));
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(!(obj instanceof HTTPResponse)) {
            return false;
        }
        HTTPResponse response = (HTTPResponse)obj;
        if(this.getStatusCode() != response.getStatusCode()) {
            return false;
        }
        if(!this.getHeaders().equals(response.getHeaders())) {
            return false;
        }
        if(!this.content.equals(response.content)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.statusCode;
        result = 31 * result + this.headers.hashCode();
        result = 31 * result + this.content.hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("\tStatus code: %s", getStatusCode()));
        if(!getHeaders().isEmpty()) {
            builder.append(String.format("%n\tHeaders: %s", getHeaders()));
        }
        String content = getContentAsString(LOG_MAX_CONTENT_LENGTH);
        if(!content.isEmpty()) {
            builder.append(String.format("%n\tContent: %s", content));
        }
        return builder.toString();
    }

    public static class Builder {
        private int statusCode = DEFAULT_STATUS_CODE;
        private Map<String, String> headers = Maps.newHashMap();;
        private String content = "";
        private boolean contentIsFilePath = false;

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
            this.content = content;
            this.contentIsFilePath = new File(content).exists();
            return this;
        }

        public HTTPResponse build() {
            return new HTTPResponse(statusCode, headers, content, contentIsFilePath);
        }
    }
}