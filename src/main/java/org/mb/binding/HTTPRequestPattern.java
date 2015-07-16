package org.mb.binding;

import com.google.common.collect.*;
import org.mb.http.HTTPMethod;
import org.mb.http.HTTPRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.mb.binding.Utils.*;

/**
 * Created by Dmitriy Dzhevaga on 18.06.2015.
 */
public class HTTPRequestPattern {

    private final static String DEFAULT_URI_PATTERN = ".*";
    private Pattern uriPattern;
    private Set<HTTPMethod> methods;
    private ListMultimap<String, String> queryParameters;
    private SetMultimap<String, String> headers;

    private HTTPRequestPattern() {
        this.uriPattern = Pattern.compile(DEFAULT_URI_PATTERN);
        this.methods = Sets.newHashSet();
        this.queryParameters = ArrayListMultimap.create();
        this.headers = HashMultimap.create();
    }

    public static RequestPatternBuilder getBuilder() {
        return new RequestPatternBuilder();
    }

    public static class RequestPatternBuilder {
        private final HTTPRequestPattern requestPattern = new HTTPRequestPattern();

        public RequestPatternBuilder setUriPattern(String uriPattern) {
            requestPattern.uriPattern = Pattern.compile(uriPattern);
            return this;
        }

        public RequestPatternBuilder addMethod(HTTPMethod method) {
            requestPattern.methods.add(method);
            return this;
        }

        public RequestPatternBuilder addMethod(String method) {
            addMethod(HTTPMethod.fromString(method));
            return this;
        }

        public RequestPatternBuilder addMethods(Collection<HTTPMethod> methods) {
            requestPattern.methods.addAll(methods);
            return this;
        }

        public RequestPatternBuilder addMethodsAsStrings(Collection<String> methods) {
            for(String method : methods) {
                addMethod(method);
            }
            return this;
        }

        public RequestPatternBuilder addQueryParameter(String name, String values) {
            requestPattern.queryParameters.put(name, values);
            return this;
        }

        public RequestPatternBuilder addQueryParameters(Multimap<String, String> parameters) {
            requestPattern.queryParameters.putAll(parameters);
            return this;
        }

        public RequestPatternBuilder addQueryParameters(String name, Collection<String> values) {
            requestPattern.queryParameters.putAll(name, values);
            return this;
        }

        public RequestPatternBuilder addHeader(String name, String value) {
            requestPattern.headers.put(name, value);
            return this;
        }

        public RequestPatternBuilder addHeaders(Multimap<String, String> parameters) {
            requestPattern.headers.putAll(parameters);
            return this;
        }

        public RequestPatternBuilder addHeaders(String name, Collection<String> values) {
            requestPattern.headers.putAll(name, values);
            return this;
        }

        public HTTPRequestPattern build() {
            return requestPattern;
        }
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
}