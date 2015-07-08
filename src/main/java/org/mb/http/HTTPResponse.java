package org.mb.http;

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
    private static int DEFAULT_STATUS_CODE = 200;
    private static int CONTENT_MAX_LOG_LENGTH = 1024;

    private int statusCode;
    private Map<String, String> headers;
    private String content;
    private boolean contentIsFilePath;

    private HTTPResponse() {
        this.statusCode = DEFAULT_STATUS_CODE;
        this.headers = Maps.newHashMap();
        this.content = "";
        this.contentIsFilePath = false;
    }

    public static HTTPResponseBuilder getBuilder() {return new HTTPResponseBuilder();}

    public static class HTTPResponseBuilder {
        private HTTPResponse httpResponse = new HTTPResponse();

        public HTTPResponseBuilder setStatusCode(int statusCode) {
            httpResponse.statusCode = statusCode;
            return this;
        }

        public HTTPResponseBuilder setStatusCode(String statusCode) {
            httpResponse.statusCode = Integer.valueOf(statusCode);
            return this;
        }

        public HTTPResponseBuilder addHeader(String name, String value) {
            httpResponse.headers.put(name, value);
            return this;
        }

        public HTTPResponseBuilder addHeaders(Map<String, String> parameters) {
            httpResponse.headers.putAll(parameters);
            return this;
        }

        public HTTPResponseBuilder setContent(String content) {
            httpResponse.content = content;
            httpResponse.contentIsFilePath = new File(content).exists();
            return this;
        }

        public HTTPResponse build() {
            return httpResponse;
        }
    }

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
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("\tStatus code: %s", getStatusCode()));
        if(!getHeaders().isEmpty()) {
            builder.append(String.format("%n\tHeaders: %s", getHeaders()));
        }
        String content = getContentAsString(CONTENT_MAX_LOG_LENGTH);
        if(!content.isEmpty()) {
            builder.append(String.format("%n\tContent: %s", content));
        }
        return builder.toString();
    }
}