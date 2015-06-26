package org.mb.binding;

import com.google.common.collect.*;
import org.mb.http.HTTPMethod;
import org.mb.http.HTTPRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Dmitriy Dzhevaga on 18.06.2015.
 */
public class HTTPRequestPattern {

    private final static String DEFAULT_URI_PATTERN = ".*";
    private Pattern uriPattern;
    private Set<HTTPMethod> methods;
    private ListMultimap<String, String> queryParameters;
    private SetMultimap<String, String> headers;
    private SetMultimap<String, String> contentParameters;

    private HTTPRequestPattern() {
        this.uriPattern = Pattern.compile(DEFAULT_URI_PATTERN);
        this.methods = Sets.newHashSet();
        this.queryParameters = ArrayListMultimap.create();
        this.headers = HashMultimap.create();
        this.contentParameters = HashMultimap.create();
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

        public RequestPatternBuilder addHeader(String name, String value) {
            requestPattern.headers.put(name, value);
            return this;
        }

        public RequestPatternBuilder addHeaders(Multimap<String, String> parameters) {
            requestPattern.headers.putAll(parameters);
            return this;
        }

        public RequestPatternBuilder addContentParameter(String name, String value) {
            requestPattern.contentParameters.put(name, value);
            return this;
        }

        public RequestPatternBuilder addContentParameters(Multimap<String, String> parameters) {
            requestPattern.contentParameters.putAll(parameters);
            return this;
        }

        public HTTPRequestPattern build() {
            return requestPattern;
        }
    }

    boolean matches(HTTPRequest request, Map<String, String> requestContentParameters) {
        Matcher matcher = this.uriPattern.matcher(request.getURI());
        if(!matcher.matches()) {
            return false;
        }

        if(!this.methods.isEmpty()) {
            if(!this.methods.contains(request.getMethod())) {
                return false;
            }
        }

        if(!multimapMatchesMultimap(request.getQueryParameters(), this.queryParameters)) {
            return false;
        }

        if(!multimapMatchesMap(this.headers, request.getHeaders())) {
            return false;
        }

        if(!multimapMatchesMap(this.contentParameters, requestContentParameters)) {
            return false;
        }

        return true;
    }

    private <T1, T2> boolean multimapMatchesMap(Multimap<T1, T2> multimap, Map<T1, T2> map) {
        if(multimap.isEmpty()) return true;
        for (T1 key : multimap.keySet()) {
            if(!multimap.get(key).contains(map.get(key)))
                return false;
        }
        return true;
    }

    private <T1, T2> boolean multimapMatchesMultimap(Multimap<T1, T2> ar, Multimap<T1, T2> er) {
        if(er.isEmpty()) return true;
        for (T1 key : er.keySet()) {
            if(!(er.get(key).containsAll(ar.get(key)) && ar.get(key).containsAll(er.get(key))))
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
        if(!requestPattern.uriPattern.equals(this.uriPattern)) {
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
        result = 31 * result + this.uriPattern.hashCode();
        result = 31 * result + this.contentParameters.hashCode();
        return result;
    }
}
