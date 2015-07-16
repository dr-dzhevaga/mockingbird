package org.mb.binding;

import org.junit.Assert;
import org.junit.Test;
import org.mb.http.HTTPMethod;
import org.mb.http.HTTPRequest;
import org.mb.http.HTTPResponse;

import java.util.Arrays;

/**
 * Created by Dmitriy Dzhevaga on 14.07.2015.
 */
public class HTTPBindingTest {
    private static int DEFAULT_STATUS_CODE = 404;
    private static String DEFAULT_CONTENT = "Not found";

    private HTTPResponse getDefaultResponse() {
        return HTTPResponse.getBuilder().
                setStatusCode(DEFAULT_STATUS_CODE).
                setContent(DEFAULT_CONTENT).
                build();
    }

    private HTTPResponse getResponse(int statusCode) {
        return HTTPResponse.getBuilder().setStatusCode(statusCode).build();
    }

    private HTTPRequestPattern getRequestPattern(HTTPMethod...methods) {
        return HTTPRequestPattern.getBuilder().addMethods(Arrays.asList(methods)).build();
    }

    private HTTPRequest getRequest(HTTPMethod method) {
        return HTTPRequest.getBuilder("/uri", method).build();
    }

    @Test
    public void resolve_emptyBindings_returnDefaultResponse() throws Exception {
        HTTPBinding binding = new HTTPBinding();
        HTTPRequest request = getRequest(HTTPMethod.GET);
        HTTPResponse defaultResponse = getDefaultResponse();

        HTTPResponse response = binding.resolve(request);

        Assert.assertTrue(response.equals(defaultResponse));
    }

    @Test
    public void resolve_noMatchedPattern_returnDefaultResponse() throws Exception {
        HTTPBinding binding = new HTTPBinding();
        HTTPRequestPattern requestPattern = getRequestPattern(HTTPMethod.GET);
        HTTPResponse response = getResponse(1);
        binding.addBinding(requestPattern, response);
        HTTPRequest request = getRequest(HTTPMethod.POST);

        Assert.assertFalse(response.equals(binding.resolve(request)));
    }

    @Test
    public void resolve_matchedPattern_returnProperResponse() throws Exception {
        HTTPBinding binding = new HTTPBinding();
        HTTPRequestPattern requestPattern1 = getRequestPattern(HTTPMethod.GET);
        HTTPRequestPattern requestPattern2 = getRequestPattern(HTTPMethod.POST);
        HTTPResponse response1 = getResponse(1);
        HTTPResponse response2 = getResponse(2);
        binding.addBinding(requestPattern1, response1);
        binding.addBinding(requestPattern2, response2);
        HTTPRequest request1 = getRequest(HTTPMethod.GET);
        HTTPRequest request2 = getRequest(HTTPMethod.POST);

        Assert.assertTrue(response1.equals(binding.resolve(request1)));
        Assert.assertTrue(response2.equals(binding.resolve(request2)));
    }

    @Test
    public void resolve_severalMatchedPatterns_returnFirstResponse() throws Exception {
        HTTPBinding binding = new HTTPBinding();
        HTTPRequestPattern requestPattern1 = getRequestPattern(HTTPMethod.GET);
        HTTPRequestPattern requestPattern2 = getRequestPattern(HTTPMethod.GET, HTTPMethod.POST);
        HTTPRequestPattern requestPattern3 = getRequestPattern(HTTPMethod.GET, HTTPMethod.POST, HTTPMethod.PUT);
        HTTPResponse response1 = getResponse(1);
        HTTPResponse response2 = getResponse(2);
        HTTPResponse response3 = getResponse(3);
        binding.addBinding(requestPattern1, response1);
        binding.addBinding(requestPattern2, response2);
        binding.addBinding(requestPattern3, response3);
        HTTPRequest request = getRequest(HTTPMethod.GET);

        Assert.assertTrue(response1.equals(binding.resolve(request)));
    }

    @Test
    public void setDefaultResponse_setNewDefaultResponse_changesDefaultResponse() throws Exception {
        HTTPBinding binding = new HTTPBinding();
        HTTPResponse response = getResponse(1);
        binding.setDefaultResponse(response);
        HTTPRequest request = getRequest(HTTPMethod.GET);

        Assert.assertTrue(response.equals(binding.resolve(request)));
    }
}
