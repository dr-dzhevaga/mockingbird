package mb.http;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.SetMultimap;

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
        this.methods = new HashSet<HTTPMethod>();
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

        public RequestPatternBuilder addMethods(Collection<HTTPMethod> methods) {
            requestPattern.methods.addAll(methods);
            return this;
        }

        public RequestPatternBuilder addQueryParameter(String name, String values) {
            requestPattern.queryParameters.put(name, values);
            return this;
        }

        public RequestPatternBuilder addHeader(String name, String value) {
            requestPattern.headers.put(name, value);
            return this;
        }

        public RequestPatternBuilder addContentParameter(String name, String value) {
            requestPattern.contentParameters.put(name, value);
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

        if(!matches(this.queryParameters, request.getQueryParameters())) {
            return false;
        }

        if(!matches(this.headers, request.getHeaders())) {
            return false;
        }

        if(!matches(this.contentParameters, requestContentParameters)) {
            return false;
        }

        return true;
    }

    private <T1, T2> boolean matches(SetMultimap<T1, T2> er, Map<T1, T2> ar) {
        if(er.isEmpty()) return true;
        for (T1 key : er.keySet()) {
            if(!er.get(key).contains(ar.get(key)))
                return false;
        }
        return true;
    }

    private <T1, T2> boolean matches(ListMultimap<T1, T2> er, ListMultimap<T1, T2> ar) {
        if(er.isEmpty()) return true;
        for (T1 key : er.keySet()) {
            if(!(er.get(key).containsAll(ar.get(key)) && er.get(key).containsAll(ar.get(key))))
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
