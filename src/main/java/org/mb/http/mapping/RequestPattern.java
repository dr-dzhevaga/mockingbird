package org.mb.http.mapping;

import com.google.common.collect.Sets;
import org.apache.log4j.Logger;
import org.mb.http.basic.Method;
import org.mb.http.basic.Request;
import org.mb.http.mapping.utils.RegexPatternMap;
import org.mb.http.mapping.utils.RegexPattern;

import java.util.Map;
import java.util.Set;
import java.util.Objects;
import java.util.Collection;

/**
 * Created by Dmitriy Dzhevaga on 18.06.2015.
 */
public final class RequestPattern {
    private static final String LOG_REQUEST_PATTERN = "Matching with pattern:%n%s";
    private static final String LOG_URI_NOT_MATCHED = "Uri is not matched";
    private static final String LOG_METHOD_NOT_MATCHED = "Method is not matched";
    private static final String LOG_QUERY_PARAMETER_NOT_MATCHED = "Query parameter is not matched";
    private static final String LOG_HEADER_NOT_MATCHED = "Header is not matched";
    private static final String LOG_CONTENT_NOT_MATCHED = "Content is not matched";
    private static final String LOG_MATCHED = "Request is matched";
    private static final Logger LOG = Logger.getLogger(RequestPattern.class);

    private static final String DEFAULT_URI_PATTERN = ".*";

    private final RegexPattern uriRegexPattern;
    private final Set<Method> methods;
    private final RegexPatternMap queryParameters;
    private final RegexPatternMap headers;
    private final RegexPatternMap content;

    private RequestPattern(final RegexPattern uriRegexPattern,
                           final Set<Method> methods,
                           final RegexPatternMap queryParameters,
                           final RegexPatternMap headers,
                           final RegexPatternMap content) {
        this.uriRegexPattern = uriRegexPattern;
        this.methods = methods;
        this.queryParameters = queryParameters;
        this.headers = headers;
        this.content = content;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public boolean matches(final Request request, final Map<String, String> content) {
        LOG.debug(String.format(LOG_REQUEST_PATTERN, this));

        if (!this.uriRegexPattern.matches(request.getURI())) {
            LOG.debug(LOG_URI_NOT_MATCHED);
            return false;
        }

        if (!this.methods.isEmpty()) {
            if (!this.methods.contains(request.getMethod())) {
                LOG.debug(LOG_METHOD_NOT_MATCHED);
                return false;
            }
        }

        if (!this.queryParameters.matches(request.getQueryParameters())) {
            LOG.debug(LOG_QUERY_PARAMETER_NOT_MATCHED);
            return false;
        }

        if (!this.headers.matches(request.getHeaders())) {
            LOG.debug(LOG_HEADER_NOT_MATCHED);
            return false;
        }

        if (!this.content.matches(content)) {
            LOG.debug(LOG_CONTENT_NOT_MATCHED);
            return false;
        }

        LOG.debug(LOG_MATCHED);
        return true;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RequestPattern)) {
            return false;
        }
        final RequestPattern other = (RequestPattern) obj;
        return Objects.equals(this.uriRegexPattern, other.uriRegexPattern)
                && Objects.equals(this.methods, other.methods)
                && Objects.equals(this.headers, other.headers)
                && Objects.equals(this.queryParameters, other.queryParameters)
                && Objects.equals(this.content, other.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(queryParameters, headers, methods, uriRegexPattern, content);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("\tMethod: %s", methods));
        builder.append(String.format("%n\tUri pattern: %s", uriRegexPattern));
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
        private RegexPattern uriRegexPattern = RegexPattern.from(DEFAULT_URI_PATTERN);
        private Set<Method> methods = Sets.newHashSet();
        private RegexPatternMap queryParameters = RegexPatternMap.newInstance();
        private RegexPatternMap headers = RegexPatternMap.newInstance();
        private RegexPatternMap contentParameters = RegexPatternMap.newInstance();

        public Builder setUriRegex(final String uriRegex) {
            this.uriRegexPattern = RegexPattern.from(uriRegex);
            return this;
        }

        public Builder addMethod(final Method method) {
            this.methods.add(method);
            return this;
        }

        public Builder addMethod(final String method) {
            addMethod(Method.of(method));
            return this;
        }

        public Builder addMethods(final Collection<Method> methods) {
            this.methods.addAll(methods);
            return this;
        }

        public Builder addMethodsAsStrings(final Collection<String> methods) {
            for (String method : methods) {
                addMethod(method);
            }
            return this;
        }

        public Builder addQueryParameter(final String name, final String valueRegex) {
            this.queryParameters.add(name, valueRegex);
            return this;
        }

        public Builder addQueryParameters(final Map<String, String> parameters) {
            this.queryParameters.addAll(parameters);
            return this;
        }

        public Builder addHeader(final String name, final String valueRegex) {
            this.headers.add(name, valueRegex);
            return this;
        }

        public Builder addHeaders(final Map<String, String> parameters) {
            this.headers.addAll(parameters);
            return this;
        }

        public Builder addContentParameter(final String name, final String valueRegex) {
            this.contentParameters.add(name, valueRegex);
            return this;
        }

        public Builder addContentParameters(final Map<String, String> parameters) {
            this.contentParameters.addAll(parameters);
            return this;
        }

        public RequestPattern build() {
            return new RequestPattern(uriRegexPattern, methods, queryParameters, headers, contentParameters);
        }
    }
}