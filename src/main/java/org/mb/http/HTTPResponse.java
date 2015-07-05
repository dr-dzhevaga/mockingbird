package org.mb.http;

import java.util.Collections;
import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 17.06.2015.
 */
public class HTTPResponse {
    private static int DEFAULT_STATUS_CODE = 200;
    private int statusCode;
    private Map<String, String> headers;
    private String content;

    private HTTPResponse() {
        this.statusCode = DEFAULT_STATUS_CODE;
        this.headers = Collections.emptyMap();
        this.content = "";
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

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("\tStatus code: %s", getStatusCode()));
        if(!getHeaders().isEmpty()) {
            builder.append(String.format("%n\tHeaders: %s", getHeaders()));
        }
        if(!getContent().isEmpty()) {
            builder.append(String.format("%n\tContent: %s", getContent()));
        }
        return builder.toString();
    }
}