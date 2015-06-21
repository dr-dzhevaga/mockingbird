package mb.http;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 17.06.2015.
 */
public class HTTPRequest {
    private final String uri;
    private final HTTPMethod method;
    private final ListMultimap<String, String> queryParameters;
    private final Map<String, String> headers;
    private final String content;

    public HTTPRequest(String uri, HTTPMethod method, ListMultimap<String, String> queryParameters, Map<String, String> headers, String content) {
        this.uri = uri;
        this.method = method;
        this.queryParameters = ArrayListMultimap.create(queryParameters);
        this.headers = Maps.newHashMap(headers);
        this.content = content;
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

    @Override
    public String toString() {
        return getMethod() + " to " + getURI();
    }
}