package org.mb.http;

import com.google.common.collect.*;

import java.util.Collection;
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

    public static Builder getBuilder(String uri, HTTPMethod method) {
        return new Builder(uri, method);
    }

    public static class Builder {
        private HTTPRequest httpRequest;

        private Builder(String uri, HTTPMethod method) {
            httpRequest = new HTTPRequest(uri, method);
        }

        public Builder addQueryParameter(String name, String value) {
            httpRequest.queryParameters.put(name, value);
            return this;
        }

        public Builder addQueryParameters(String name, Collection<String> values) {
            httpRequest.queryParameters.putAll(name, values);
            return this;
        }

        public Builder addHeader(String name, String value) {
            httpRequest.headers.put(name, value);
            return this;
        }

        public Builder addHeaders(Map<String, String> parameters) {
            httpRequest.headers.putAll(parameters);
            return this;
        }

        public Builder setContent(String content) {
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