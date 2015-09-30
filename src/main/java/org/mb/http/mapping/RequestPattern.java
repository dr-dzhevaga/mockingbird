package org.mb.http.mapping;

import com.google.common.collect.*;
import org.apache.log4j.Logger;
import org.mb.http.basic.Method;
import org.mb.http.basic.Request;
import org.mb.http.mapping.utils.RegexPatternMap;
import org.mb.http.mapping.utils.RegexPattern;

import java.util.*;

/**
 * Created by Dmitriy Dzhevaga on 18.06.2015.
 */
public class RequestPattern {
    private static final String LOG_REQUEST_PATTERN = "Matching with pattern:\n%s";
    private static final String LOG_URI_NOT_MATCHED = "Uri is not matched";
    private static final String LOG_METHOD_NOT_MATCHED = "Method is not matched";
    private static final String LOG_QUERY_PARAMETER_NOT_MATCHED = "Query parameter is not matched";
    private static final String LOG_HEADER_NOT_MATCHED = "Header is not matched";
    private static final String LOG_CONTENT_NOT_MATCHED = "Content is not matched";
    private static final String LOG_MATCHED = "Request is matched";
    private static final Logger Log = Logger.getLogger(RequestPattern.class);

    private static final String DEFAULT_URI_PATTERN = ".*";

    final private RegexPattern uriRegexPattern;
    final private Set<Method> methods;
    final private RegexPatternMap queryParameters;
    final private RegexPatternMap headers;
    final private RegexPatternMap content;

    private RequestPattern(RegexPattern uriRegexPattern, Set<Method> methods, RegexPatternMap queryParameters, RegexPatternMap headers, RegexPatternMap content) {
        this.uriRegexPattern = uriRegexPattern;
        this.methods = methods;
        this.queryParameters = queryParameters;
        this.headers = headers;
        this.content = content;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public boolean matches(Request request, Map<String, String> content) {
        Log.debug(String.format(LOG_REQUEST_PATTERN, this));

        if(!this.uriRegexPattern.matches(request.getURI())) {
            Log.debug(LOG_URI_NOT_MATCHED);
            return false;
        }

        if(!this.methods.isEmpty()) {
            if(!this.methods.contains(request.getMethod())) {
                Log.debug(LOG_METHOD_NOT_MATCHED);
                return false;
            }
        }

        if(!this.queryParameters.matches(request.getQueryParameters())) {
            Log.debug(LOG_QUERY_PARAMETER_NOT_MATCHED);
            return false;
        }

        if(!this.headers.matches(request.getHeaders())) {
            Log.debug(LOG_HEADER_NOT_MATCHED);
            return false;
        }

        if(!this.content.matches(content)) {
            Log.debug(LOG_CONTENT_NOT_MATCHED);
            return false;
        }

        Log.debug(LOG_MATCHED);
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(!(obj instanceof RequestPattern)) {
            return false;
        }
        final RequestPattern other = (RequestPattern)obj;
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
        builder.append(String.format("\n\tUri pattern: %s", uriRegexPattern));
        if(!queryParameters.isEmpty()) {
            builder.append(String.format("\n\tQuery parameters: %s", queryParameters));
        }
        if(!headers.isEmpty()) {
            builder.append(String.format("\n\tHeaders: %s", headers));
        }
        if(!content.isEmpty()) {
            builder.append(String.format("\n\tContent: %s", content));
        }
        return builder.toString();
    }

    public static class Builder {
        private RegexPattern uriRegexPattern = RegexPattern.from(DEFAULT_URI_PATTERN);
        private Set<Method> methods = Sets.newHashSet();
        private RegexPatternMap queryParameters = RegexPatternMap.newInstance();
        private RegexPatternMap headers = RegexPatternMap.newInstance();
        private RegexPatternMap contentParameters = RegexPatternMap.newInstance();

        public Builder setUriRegex(String uriRegex) {
            this.uriRegexPattern = RegexPattern.from(uriRegex);
            return this;
        }

        public Builder addMethod(Method method) {
            this.methods.add(method);
            return this;
        }

        public Builder addMethod(String method) {
            addMethod(Method.of(method));
            return this;
        }

        public Builder addMethods(Collection<Method> methods) {
            this.methods.addAll(methods);
            return this;
        }

        public Builder addMethodsAsStrings(Collection<String> methods) {
            for(String method : methods) {
                addMethod(method);
            }
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

        public RequestPattern build() {
            return new RequestPattern(uriRegexPattern, methods, queryParameters, headers, contentParameters);
        }
    }
}