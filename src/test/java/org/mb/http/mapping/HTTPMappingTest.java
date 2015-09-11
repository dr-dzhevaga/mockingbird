package org.mb.http.mapping;

import org.junit.Assert;
import org.junit.Test;
import org.mb.http.HTTPMethod;
import org.mb.http.HTTPRequest;
import org.mb.http.HTTPResponse;

import java.util.Arrays;

/**
 * Created by Dmitriy Dzhevaga on 14.07.2015.
 */
public class HTTPMappingTest {
    private static int DEFAULT_STATUS_CODE = 404;
    private static String DEFAULT_CONTENT = "Not found";

    private HTTPResponse getDefaultResponse() {
        return HTTPResponse.newBuilder().
                setStatusCode(DEFAULT_STATUS_CODE).
                setContent(DEFAULT_CONTENT).
                build();
    }

    private HTTPResponse getResponse(int statusCode) {
        return HTTPResponse.newBuilder().setStatusCode(statusCode).build();
    }

    private HTTPRequestPattern getRequestPattern(HTTPMethod...methods) {
        return HTTPRequestPattern.newBuilder().addMethods(Arrays.asList(methods)).build();
    }

    private HTTPRequest getRequest(HTTPMethod method) {
        return HTTPRequest.newBuilder("/uri", method).build();
    }

    @Test
    public void resolve_emptyMapping_returnDefaultResponse() throws Exception {
        HTTPRequestResponseMapping mapping = new HTTPRequestResponseMapping();
        HTTPRequest request = getRequest(HTTPMethod.GET);
        HTTPResponse defaultResponse = getDefaultResponse();

        HTTPResponse response = mapping.findResponse(request);

        Assert.assertTrue(response.equals(defaultResponse));
    }

    @Test
    public void resolve_noMatchedPattern_returnDefaultResponse() throws Exception {
        HTTPRequestResponseMapping mapping = new HTTPRequestResponseMapping();
        HTTPRequestPattern requestPattern = getRequestPattern(HTTPMethod.GET);
        HTTPResponse response = getResponse(1);
        mapping.addMapping(requestPattern, response);
        HTTPRequest request = getRequest(HTTPMethod.POST);

        Assert.assertFalse(response.equals(mapping.findResponse(request)));
    }

    @Test
    public void resolve_matchedPattern_returnProperResponse() throws Exception {
        HTTPRequestResponseMapping mapping = new HTTPRequestResponseMapping();
        HTTPRequestPattern requestPattern1 = getRequestPattern(HTTPMethod.GET);
        HTTPRequestPattern requestPattern2 = getRequestPattern(HTTPMethod.POST);
        HTTPResponse response1 = getResponse(1);
        HTTPResponse response2 = getResponse(2);
        mapping.addMapping(requestPattern1, response1);
        mapping.addMapping(requestPattern2, response2);
        HTTPRequest request1 = getRequest(HTTPMethod.GET);
        HTTPRequest request2 = getRequest(HTTPMethod.POST);

        Assert.assertTrue(response1.equals(mapping.findResponse(request1)));
        Assert.assertTrue(response2.equals(mapping.findResponse(request2)));
    }

    @Test
    public void resolve_severalMatchedPatterns_returnFirstResponse() throws Exception {
        HTTPRequestResponseMapping mapping = new HTTPRequestResponseMapping();
        HTTPRequestPattern requestPattern1 = getRequestPattern(HTTPMethod.GET);
        HTTPRequestPattern requestPattern2 = getRequestPattern(HTTPMethod.GET, HTTPMethod.POST);
        HTTPRequestPattern requestPattern3 = getRequestPattern(HTTPMethod.GET, HTTPMethod.POST, HTTPMethod.PUT);
        HTTPResponse response1 = getResponse(1);
        HTTPResponse response2 = getResponse(2);
        HTTPResponse response3 = getResponse(3);
        mapping.addMapping(requestPattern1, response1);
        mapping.addMapping(requestPattern2, response2);
        mapping.addMapping(requestPattern3, response3);
        HTTPRequest request = getRequest(HTTPMethod.GET);

        Assert.assertTrue(response1.equals(mapping.findResponse(request)));
    }

    @Test
    public void setDefaultResponse_setNewDefaultResponse_changesDefaultResponse() throws Exception {
        HTTPRequestResponseMapping mapping = new HTTPRequestResponseMapping();
        HTTPResponse response = getResponse(1);
        mapping.setDefaultResponse(response);
        HTTPRequest request = getRequest(HTTPMethod.GET);

        Assert.assertTrue(response.equals(mapping.findResponse(request)));
    }
}
