package org.mb.http;

import com.google.common.collect.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 17.06.2015.
 */
public class HTTPRequest {
    private String uri;
    private HTTPMethod method;
    private ListMultimap<String, String> queryParameters;
    private Map<String, String> headers;
    private String content;

    private HTTPRequest(String uri, HTTPMethod method) {
        this.uri = uri;
        this.method = method;
        this.queryParameters = ArrayListMultimap.create();
        this.headers = Maps.newHashMap();
        this.content = "";
    }

    public static HTTPRequestBuilder getBuilder(String uri, HTTPMethod method) {
        return new HTTPRequestBuilder(uri, method);
    }

    public static class HTTPRequestBuilder {
        private HTTPRequest httpRequest;

        private HTTPRequestBuilder(String uri, HTTPMethod method) {
            httpRequest = new HTTPRequest(uri, method);
        }

        public HTTPRequestBuilder addQueryParameter(String name, String value) {
            httpRequest.queryParameters.put(name, value);
            return this;
        }

        public HTTPRequestBuilder addQueryParameters(String name, Collection<String> values) {
            httpRequest.queryParameters.putAll(name, values);
            return this;
        }

        public HTTPRequestBuilder addHeader(String name, String value) {
            httpRequest.headers.put(name, value);
            return this;
        }

        public HTTPRequestBuilder addHeaders(Map<String, String> parameters) {
            httpRequest.headers.putAll(parameters);
            return this;
        }

        public HTTPRequestBuilder setContent(String content) {
            httpRequest.content = content;
            return this;
        }

        public HTTPRequest build() {
            return httpRequest;
        }
    }

    public String getURI() {
        return uri;
    }

    public HTTPMethod getMethod() {
        return method;
    }

    public ListMultimap<String, String> getQueryParameters() {
        return queryParameters;
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
        builder.append(String.format("\tMethod: %s", getMethod()));
        builder.append(String.format("%n\tURI: %s", getURI()));
        if(!getQueryParameters().isEmpty()) {
            builder.append(String.format("%n\tQuery parameters: %s", getQueryParameters()));
        }
        if(!getHeaders().isEmpty()) {
            builder.append(String.format("%n\tHeaders: %s", getHeaders()));
        }
        if(!getContent().isEmpty()) {
            builder.append(String.format("%n\tContent: %s", getContent()));
        }
        return builder.toString();
    }
}