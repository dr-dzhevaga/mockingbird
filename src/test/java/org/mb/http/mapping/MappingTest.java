package org.mb.http.mapping;

import com.google.common.collect.Maps;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.mb.http.basic.Method;
import org.mb.http.basic.Request;
import org.mb.http.basic.Response;
import org.mb.parsing.Parsing;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 14.07.2015.
 */
public class MappingTest {
    private static int DEFAULT_STATUS_CODE = 404;
    private static String DEFAULT_CONTENT = "Not found";

    static
    {
        Logger.getLogger("org.mb").setLevel(Level.ERROR);
    }

    private Response getDefaultResponse() {
        return Response.newBuilder().
                setStatusCode(DEFAULT_STATUS_CODE).
                setContent(DEFAULT_CONTENT).
                build();
    }

    private Response getResponse(int statusCode) {
        return Response.newBuilder().setStatusCode(statusCode).build();
    }

    private RequestPattern getRequestPattern(Method...methods) {
        return RequestPattern.newBuilder().addMethods(Arrays.asList(methods)).build();
    }

    private Request getRequest(Method method) {
        return Request.newBuilder("/uri", method).build();
    }

    private Map<String, String> getContent() {
        return Maps.newHashMap();
    }

    private Parsing getBulkParser() {
        return new Parsing();
    }

    @Test
    public void resolve_emptyMapping_returnDefaultResponse() throws Exception {
        Mapping mapping = new Mapping();
        Request request = getRequest(Method.GET);
        Response defaultResponse = getDefaultResponse();

        Response response = mapping.resolve(request, getContent()).getResponse();

        Assert.assertTrue(response.equals(defaultResponse));
    }

    @Test
    public void resolve_noMatchedPattern_returnDefaultResponse() throws Exception {
        Mapping mapping = new Mapping();
        RequestPattern requestPattern = getRequestPattern(Method.GET);
        Response responseER = getResponse(1);
        mapping.addMapping(requestPattern, responseER, getBulkParser());
        Request request = getRequest(Method.POST);

        Response responseAR = mapping.resolve(request, getContent()).getResponse();

        Assert.assertFalse(responseER.equals(responseAR));
    }

    @Test
    public void resolve_matchedPattern_returnProperResponse() throws Exception {
        Mapping mapping = new Mapping();
        RequestPattern requestPattern1 = getRequestPattern(Method.GET);
        RequestPattern requestPattern2 = getRequestPattern(Method.POST);
        Response responseER1 = getResponse(1);
        Response responseER2 = getResponse(2);
        mapping.addMapping(requestPattern1, responseER1, getBulkParser());
        mapping.addMapping(requestPattern2, responseER2, getBulkParser());
        Request request1 = getRequest(Method.GET);
        Request request2 = getRequest(Method.POST);
        Response responseAR1 = mapping.resolve(request1, getContent()).getResponse();
        Response responseAR2 = mapping.resolve(request2, getContent()).getResponse();

        Assert.assertTrue(responseER1.equals(responseAR1));
        Assert.assertTrue(responseER2.equals(responseAR2));
    }

    @Test
    public void resolve_severalMatchedPatterns_returnFirstPropperResponse() throws Exception {
        Mapping mapping = new Mapping();
        RequestPattern requestPattern1 = getRequestPattern(Method.GET);
        RequestPattern requestPattern2 = getRequestPattern(Method.GET, Method.POST);
        RequestPattern requestPattern3 = getRequestPattern(Method.GET, Method.POST, Method.PUT);
        Response responseER = getResponse(1);
        Response response1 = getResponse(2);
        Response response2 = getResponse(3);
        mapping.addMapping(requestPattern1, responseER, getBulkParser());
        mapping.addMapping(requestPattern2, response1, getBulkParser());
        mapping.addMapping(requestPattern3, response2, getBulkParser());
        Request request = getRequest(Method.GET);

        Response responseAR = mapping.resolve(request, getContent()).getResponse();

        Assert.assertTrue(responseER.equals(responseAR));
    }
}
