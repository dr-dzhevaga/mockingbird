package org.mb.http.mapping;

import com.google.common.collect.*;
import org.mb.http.basic.HTTPMethod;
import org.mb.http.basic.HTTPRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.mb.http.mapping.Utils.*;

/**
 * Created by Dmitriy Dzhevaga on 18.06.2015.
 */
public class HTTPRequestPattern {
    private static final String DEFAULT_URI_PATTERN = ".*";

    final private Pattern uriPattern;
    final private Set<HTTPMethod> methods;
    final private ListMultimap<String, String> queryParameters;
    final private SetMultimap<String, String> headers;

    public HTTPRequestPattern(Pattern uriPattern, Set<HTTPMethod> methods, ListMultimap<String, String> queryParameters, SetMultimap<String, String> headers) {
        this.uriPattern = uriPattern;
        this.methods = methods;
        this.queryParameters = queryParameters;
        this.headers = headers;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    boolean matches(HTTPRequest request) {
        Matcher matcher = this.uriPattern.matcher(request.getURI());
        if(!matcher.matches()) {
            return false;
        }

        if(!this.methods.isEmpty()) {
            if(!this.methods.contains(request.getMethod())) {
                return false;
            }
        }

        if(!checkMultimap(request.getQueryParameters(), this.queryParameters)) {
            return false;
        }

        if(!checkMap(request.getHeaders(), this.headers)) {
            return false;
        }

        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }

        if(!(obj instanceof HTTPRequestPattern)) {
            return false;
        }

        HTTPRequestPattern requestPattern = (HTTPRequestPattern)obj;
        if(!requestPattern.uriPattern.pattern().equals(this.uriPattern.pattern())) {
            return false;
        }
        if(!requestPattern.methods.equals(this.methods)) {
            return false;
        }
        if(!requestPattern.headers.equals(this.headers)) {
            return false;
        }
        if(!requestPattern.queryParameters.equals(this.queryParameters)) {
            return false;
        }
        if(!requestPattern.queryParameters.equals(this.queryParameters)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.queryParameters.hashCode();
        result = 31 * result + this.headers.hashCode();
        result = 31 * result + this.methods.hashCode();
        result = 31 * result + this.uriPattern.pattern().hashCode();
        return result;
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
        return builder.toString();
    }

    public static class Builder {
        private Pattern uriPattern = Pattern.compile(DEFAULT_URI_PATTERN);
        private Set<HTTPMethod> methods = Sets.newHashSet();
        private ListMultimap<String, String> queryParameters = ArrayListMultimap.create();
        private SetMultimap<String, String> headers = HashMultimap.create();

        public Builder setUriPattern(String uriPattern) {
            this.uriPattern = Pattern.compile(uriPattern);
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

        public HTTPRequestPattern build() {
            return new HTTPRequestPattern(uriPattern, methods, queryParameters, headers);
        }
    }
}