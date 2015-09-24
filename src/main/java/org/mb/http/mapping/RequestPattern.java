package org.mb.http.mapping;

import com.google.common.collect.*;
import org.apache.log4j.Logger;
import org.mb.http.basic.Method;
import org.mb.http.basic.Request;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Dmitriy Dzhevaga on 18.06.2015.
 */
public class RequestPattern {
    private static final String LOG_REQUEST_PATTERN = "Matching request with pattern:\n%s";
    private static final String LOG_NOT_MATCHED = "%s is not matched";
    private static final String LOG_MATCHED = "Request is matched";
    public static final String URI = "Uri";
    public static final String METHOD = "Method";
    public static final String QUERY_PARAMETER = "Query parameter";
    public static final String HEADER = "Header";
    public static final String CONTENT = "Content";
    private static final Logger Log = Logger.getLogger(RequestPattern.class);

    private static final String DEFAULT_URI_PATTERN = ".*";

    final private Pattern uriPattern;
    final private Set<Method> methods;
    final private ListMultimap<String, String> queryParameters;
    final private SetMultimap<String, String> headers;
    final private Multimap<String, String> content;

    private RequestPattern(Pattern uriPattern, Set<Method> methods, ListMultimap<String, String> queryParameters, SetMultimap<String, String> headers, Multimap<String, String> content) {
        this.uriPattern = uriPattern;
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

        Matcher matcher = this.uriPattern.matcher(request.getURI());
        if(!matcher.matches()) {
            Log.debug(String.format(LOG_NOT_MATCHED, URI));
            return false;
        }

        if(!this.methods.isEmpty()) {
            if(!this.methods.contains(request.getMethod())) {
                Log.debug(String.format(LOG_NOT_MATCHED, METHOD));
                return false;
            }
        }

        if(!MultimapPattern.fromMultimap(this.queryParameters).matches(request.getQueryParameters())) {
            Log.debug(String.format(LOG_NOT_MATCHED, QUERY_PARAMETER));
            return false;
        }

        if(!MultimapPattern.fromMultimap(this.headers).matches(request.getHeaders())) {
            Log.debug(String.format(LOG_NOT_MATCHED, HEADER));
            return false;
        }

        if(!MultimapPattern.fromMultimap(this.content).matches(content)) {
            Log.debug(String.format(LOG_NOT_MATCHED, CONTENT));
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
        return Objects.equals(this.uriPattern.pattern(), other.uriPattern.pattern())
                && Objects.equals(this.methods, other.methods)
                && Objects.equals(this.headers, other.headers)
                && Objects.equals(this.queryParameters, other.queryParameters)
                && Objects.equals(this.content, other.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(queryParameters, headers, methods, uriPattern.pattern(), content);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("\tMethod: %s", methods));
        builder.append(String.format("%n\tURI pattern: %s", uriPattern));
        if(!queryParameters.isEmpty()) {
            builder.append(String.format("%n\tQuery parameters: %s", queryParameters));
        }
        if(!headers.isEmpty()) {
            builder.append(String.format("%n\tHeaders: %s", headers));
        }
        if(!content.isEmpty()) {
            builder.append(String.format("%n\tContent: %s", content));
        }
        return builder.toString();
    }

    public static class Builder {
        private java.util.regex.Pattern uriPattern = java.util.regex.Pattern.compile(DEFAULT_URI_PATTERN);
        private Set<Method> methods = Sets.newHashSet();
        private ListMultimap<String, String> queryParameters = ArrayListMultimap.create();
        private SetMultimap<String, String> headers = HashMultimap.create();
        private ListMultimap<String, String> content = ArrayListMultimap.create();

        public Builder setUriPattern(String uriPattern) {
            this.uriPattern = java.util.regex.Pattern.compile(uriPattern);
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

        public Builder addQueryParameter(String name, String values) {
            this.queryParameters.put(name, values);
            return this;
        }

        public Builder addQueryParameters(Multimap<String, String> parameters) {
            this.queryParameters.putAll(parameters);
            return this;
        }

        public Builder addQueryParameters(String name, Collection<String> values) {
            this.queryParameters.putAll(name, values);
            return this;
        }

        public Builder addHeader(String name, String value) {
            this.headers.put(name, value);
            return this;
        }

        public Builder addHeaders(Multimap<String, String> parameters) {
            this.headers.putAll(parameters);
            return this;
        }

        public Builder addHeaders(String name, Collection<String> values) {
            this.headers.putAll(name, values);
            return this;
        }

        public Builder addContentParameter(String name, String value) {
            content.put(name, value);
            return this;
        }

        public Builder addContentParameters(String name, Collection<String> values) {
            content.putAll(name, values);
            return this;
        }

        public Builder addContentParameters(Multimap<String, String> parameters) {
            content.putAll(parameters);
            return this;
        }

        public RequestPattern build() {
            return new RequestPattern(uriPattern, methods, queryParameters, headers, content);
        }
    }
}