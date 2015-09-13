package org.mb.http.mapping;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import org.junit.Assert;
import org.junit.Test;
import org.mb.http.basic.Method;
import org.mb.http.basic.Request;
import org.mb.http.basic.Response;
import org.mb.parsing.ParserType;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 14.07.2015.
 */
// TODO: test content matching if depends on it
public class HandlerDataMappingTest {
    private static int DEFAULT_STATUS_CODE = 404;
    private static String DEFAULT_CONTENT = "Not found";

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

    private Table<ParserType, String, String> getParsing() {
        return HashBasedTable.create();
    }

    @Test
    public void resolve_emptyMapping_returnDefaultResponse() throws Exception {
        HandlerDataMapping mapping = new HandlerDataMapping();
        Request request = getRequest(Method.GET);
        Response defaultResponse = getDefaultResponse();

        Response response = mapping.find(request, getContent()).getResponse();

        Assert.assertTrue(response.equals(defaultResponse));
    }

    @Test
    public void resolve_noMatchedPattern_returnDefaultResponse() throws Exception {
        HandlerDataMapping mapping = new HandlerDataMapping();
        RequestPattern requestPattern = getRequestPattern(Method.GET);
        Response response = getResponse(1);
        mapping.addMapping(requestPattern, response, getParsing());
        Request request = getRequest(Method.POST);

        Assert.assertFalse(response.equals(mapping.find(request, getContent())));
    }

    @Test
    public void resolve_matchedPattern_returnProperResponse() throws Exception {
        HandlerDataMapping mapping = new HandlerDataMapping();
        RequestPattern requestPattern1 = getRequestPattern(Method.GET);
        RequestPattern requestPattern2 = getRequestPattern(Method.POST);
        Response response1 = getResponse(1);
        Response response2 = getResponse(2);
        mapping.addMapping(requestPattern1, response1, getParsing());
        mapping.addMapping(requestPattern2, response2, getParsing());
        Request request1 = getRequest(Method.GET);
        Request request2 = getRequest(Method.POST);

        Assert.assertTrue(response1.equals(mapping.find(request1, getContent())));
        Assert.assertTrue(response2.equals(mapping.find(request2, getContent())));
    }

    @Test
    public void resolve_severalMatchedPatterns_returnFirstResponse() throws Exception {
        HandlerDataMapping mapping = new HandlerDataMapping();
        RequestPattern requestPattern1 = getRequestPattern(Method.GET);
        RequestPattern requestPattern2 = getRequestPattern(Method.GET, Method.POST);
        RequestPattern requestPattern3 = getRequestPattern(Method.GET, Method.POST, Method.PUT);
        Response response1 = getResponse(1);
        Response response2 = getResponse(2);
        Response response3 = getResponse(3);
        mapping.addMapping(requestPattern1, response1, getParsing());
        mapping.addMapping(requestPattern2, response2, getParsing());
        mapping.addMapping(requestPattern3, response3, getParsing());
        Request request = getRequest(Method.GET);

        Assert.assertTrue(response1.equals(mapping.find(request, getContent())));
    }
}
