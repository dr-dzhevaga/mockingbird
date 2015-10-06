package org.mb.http.basic;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Objects;

/**
 * Created by Dmitriy Dzhevaga on 17.06.2015.
 */
public final class Response {
    private static final int DEFAULT_STATUS_CODE    = 200;

    private final int statusCode;
    private final Map<String, String> headers;
    private final Content content;

    private Response(int statusCode, Map<String, String> headers, Content content) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.content = content;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Content getContent() {
        return content;
    }

    public Response setContent(Content content) {
        return new Response(this.getStatusCode(), this.getHeaders(), content);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Response)) {
            return false;
        }
        final Response other = (Response) obj;
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
        String content = getContent().toString();
        if (!content.isEmpty()) {
            builder.append(String.format("%n\tContent: %s", content));
        }
        return builder.toString();
    }

    public static final class Builder {
        private int statusCode = DEFAULT_STATUS_CODE;
        private Map<String, String> headers = Maps.newHashMap();;
        private Content content = new DefaultContent("");

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
            this.content = new DefaultContent(content);
            return this;
        }

        public Response build() {
            return new Response(statusCode, headers, content);
        }
    }
}