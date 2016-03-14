package org.mb.http.mapping;

import com.google.common.collect.Sets;
import org.apache.log4j.Logger;
import org.mb.http.basic.HTTPMethod;
import org.mb.http.basic.HTTPRequest;
import org.mb.http.mapping.utils.MapRegexMatcher;
import org.mb.http.mapping.utils.RegexMatcher;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by Dmitriy Dzhevaga on 18.06.2015.
 */
public final class HTTPRequestPattern {
    private static final Logger log = Logger.getLogger(HTTPRequestPattern.class);

    private final RegexMatcher uri;
    private final Set<HTTPMethod> methods;
    private final MapRegexMatcher queryParameters;
    private final MapRegexMatcher headers;
    private final MapRegexMatcher content;

    private HTTPRequestPattern(RegexMatcher uri,
                               Set<HTTPMethod> methods,
                               MapRegexMatcher queryParameters,
                               MapRegexMatcher headers,
                               MapRegexMatcher content) {
        this.uri = uri;
        this.methods = methods;
        this.queryParameters = queryParameters;
        this.headers = headers;
        this.content = content;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean matches(HTTPRequest request, Map<String, String> content) {
        log.debug(String.format("Matching with pattern:%n%s", this));
        if (!this.uri.matches(request.getURI())) {
            log.debug("Uri is not matched");
            return false;
        }
        if (!this.methods.isEmpty()) {
            if (!this.methods.contains(request.getMethod())) {
                log.debug("Method is not matched");
                return false;
            }
        }
        if (!this.queryParameters.matches(request.getQueryParameters())) {
            log.debug("Query parameter is not matched");
            return false;
        }
        if (!this.headers.matches(request.getHeaders())) {
            log.debug("Header is not matched");
            return false;
        }
        if (!this.content.matches(content)) {
            log.debug("Content is not matched");
            return false;
        }
        log.debug("Request is matched");
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HTTPRequestPattern)) {
            return false;
        }
        HTTPRequestPattern other = (HTTPRequestPattern) obj;
        return Objects.equals(this.uri, other.uri)
                && Objects.equals(this.methods, other.methods)
                && Objects.equals(this.headers, other.headers)
                && Objects.equals(this.queryParameters, other.queryParameters)
                && Objects.equals(this.content, other.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(queryParameters, headers, methods, uri, content);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("\tMethod: %s", methods));
        builder.append(String.format("%n\tUri pattern: %s", uri));
        if (!queryParameters.isEmpty()) {
            builder.append(String.format("%n\tQuery parameters: %s", queryParameters));
        }
        if (!headers.isEmpty()) {
            builder.append(String.format("%n\tHeaders: %s", headers));
        }
        if (!content.isEmpty()) {
            builder.append(String.format("%n\tContent: %s", content));
        }
        return builder.toString();
    }

    public static final class Builder {
        private RegexMatcher uri = RegexMatcher.from(".*");
        private Set<HTTPMethod> methods = Sets.newHashSet();
        private MapRegexMatcher queryParameters = MapRegexMatcher.newInstance();
        private MapRegexMatcher headers = MapRegexMatcher.newInstance();
        private MapRegexMatcher contentParameters = MapRegexMatcher.newInstance();

        public Builder setUriRegex(String uriRegex) {
            this.uri = RegexMatcher.from(uriRegex);
            return this;
        }

        public Builder addMethod(HTTPMethod method) {
            this.methods.add(method);
            return this;
        }

        public Builder addMethod(String method) {
            addMethod(HTTPMethod.of(method));
            return this;
        }

        public Builder addMethods(Collection<HTTPMethod> methods) {
            this.methods.addAll(methods);
            return this;
        }

        public Builder addMethodsAsStrings(Collection<String> methods) {
            methods.stream().forEach(this::addMethod);
            return this;
        }

        public Builder addQueryParameter(String name, String valueRegex) {
            this.queryParameters.add(name, valueRegex);
            return this;
        }

        public Builder addQueryParameters(Map<String, String> parameters) {
            this.queryParameters.addAll(parameters);
            return this;
        }

        public Builder addHeader(String name, String valueRegex) {
            this.headers.add(name, valueRegex);
            return this;
        }

        public Builder addHeaders(Map<String, String> parameters) {
            this.headers.addAll(parameters);
            return this;
        }

        public Builder addContentParameter(String name, String valueRegex) {
            this.contentParameters.add(name, valueRegex);
            return this;
        }

        public Builder addContentParameters(Map<String, String> parameters) {
            this.contentParameters.addAll(parameters);
            return this;
        }

        public HTTPRequestPattern build() {
            return new HTTPRequestPattern(uri, methods, queryParameters, headers, contentParameters);
        }
    }
}