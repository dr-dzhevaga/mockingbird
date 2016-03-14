package org.mb.http.basic;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by Dmitriy Dzhevaga on 17.06.2015.
 */
public final class HTTPRequest {
    private final String uri;
    private final HTTPMethod method;
    private final ListMultimap<String, String> queryParameters;
    private final Map<String, String> headers;
    private final String content;

    private HTTPRequest(String uri,
                        HTTPMethod method,
                        ListMultimap<String, String> queryParameters,
                        Map<String, String> headers,
                        String content) {
        this.uri = uri;
        this.method = method;
        this.queryParameters = queryParameters;
        this.headers = headers;
        this.content = content;
    }

    public static Builder builder(String uri, HTTPMethod method) {
        return new Builder(uri, method);
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

    public Map<String, Object> toMap() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("uri", uri);
        map.put("method", method.toString());
        map.put("header", headers);
        map.put("query", queryParameters.asMap());
        map.put("content", content);
        return map;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("\tMethod: %s", getMethod()));
        builder.append(String.format("%n\tURI: %s", getURI()));
        if (!getQueryParameters().isEmpty()) {
            builder.append(String.format("%n\tQuery parameters: %s", getQueryParameters()));
        }
        if (!getHeaders().isEmpty()) {
            builder.append(String.format("%n\tHeaders: %s", getHeaders()));
        }
        if (!getContent().isEmpty()) {
            builder.append(String.format("%n\tContent: %s", getContent()));
        }
        return builder.toString();
    }

    public static final class Builder {
        private String uri;
        private HTTPMethod method;
        private ListMultimap<String, String> queryParameters = ArrayListMultimap.create();
        private Map<String, String> headers = Maps.newHashMap();
        private String content = "";

        private Builder(String uri, HTTPMethod method) {
            this.uri = uri;
            this.method = method;
        }

        public Builder addQueryParameter(String name, String value) {
            this.queryParameters.put(name, value);
            return this;
        }

        public Builder addQueryParameters(String name, Collection<String> values) {
            this.queryParameters.putAll(name, values);
            return this;
        }

        public Builder addQueryParameters(String name, String[] values) {
            Stream.of(values).forEach(value -> this.queryParameters.put(name, value));
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
            return this;
        }

        public HTTPRequest build() {
            return new HTTPRequest(uri, method, queryParameters, headers, content);
        }
    }
}