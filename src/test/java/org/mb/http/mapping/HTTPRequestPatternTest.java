package org.mb.http.mapping;

import org.junit.Assert;
import org.junit.Test;
import org.mb.http.basic.HTTPMethod;
import org.mb.http.basic.HTTPRequest;
import static java.util.Arrays.asList;

/**
 * Created by Dmitriy Dzhevaga on 12.07.2015.
 */
public class HTTPRequestPatternTest {

    private HTTPRequestPattern getEmptyRequestPattern() {
        return HTTPRequestPattern.newBuilder().build();
    }

    private HTTPRequestPattern getFilledRequestPattern() {
        return HTTPRequestPattern.newBuilder().
                setUriPattern("/uri").
                addMethod(HTTPMethod.GET).
                addQueryParameter("1", "1").
                addHeader("1", "1").
                build();
    }

    @Test
    public void matches_defaultPattern_returnTrue() throws Exception {
        HTTPRequestPattern requestPattern = getEmptyRequestPattern();
        HTTPRequest request = HTTPRequest.newBuilder("/uri", HTTPMethod.GET).build();

        boolean result = requestPattern.matches(request);

        Assert.assertTrue(result);
    }

    @Test
    public void matches_requestEqualsPattern_returnTrue() throws Exception {
        HTTPRequestPattern requestPattern = getFilledRequestPattern();
        HTTPRequest request = HTTPRequest.newBuilder("/uri", HTTPMethod.GET).
                addQueryParameter("1", "1").
                addHeader("1", "1").
                setContent("content").
                build();

        boolean result = requestPattern.matches(request);

        Assert.assertTrue(result);
    }

    @Test
    public void matches_patternContainsRequest_returnTrue() throws Exception {
        HTTPRequestPattern requestPattern = HTTPRequestPattern.newBuilder().
                setUriPattern("/uri/.*").
                addMethods(asList(HTTPMethod.GET, HTTPMethod.POST)).
                addHeaders("1", asList("1", "2")).
                build();
        HTTPRequest request = HTTPRequest.newBuilder("/uri/uri", HTTPMethod.GET).
                addHeader("1", "1").
                setContent("content").
                build();

        boolean result = requestPattern.matches(request);

        Assert.assertTrue(result);
    }

    @Test
    public void matches_wrongUri_returnFalse() throws Exception {
        HTTPRequestPattern requestPattern = HTTPRequestPattern.newBuilder().
                setUriPattern("/uri/uri").
                build();
        HTTPRequest request = HTTPRequest.newBuilder("/uri", HTTPMethod.GET).build();

        boolean result = requestPattern.matches(request);

        Assert.assertFalse(result);
    }

    @Test
    public void matches_wrongMethod_returnFalse() throws Exception {
        HTTPRequestPattern requestPattern = HTTPRequestPattern.newBuilder().
                addMethod(HTTPMethod.GET).
                build();
        HTTPRequest request = HTTPRequest.newBuilder("/uri", HTTPMethod.POST).build();

        boolean result = requestPattern.matches(request);

        Assert.assertFalse(result);
    }

    @Test
    public void matches_missingQueryParameter_returnFalse() throws Exception {
        HTTPRequestPattern requestPattern = HTTPRequestPattern.newBuilder().
                addQueryParameter("1", "1").
                build();
        HTTPRequest request = HTTPRequest.newBuilder("/uri", HTTPMethod.POST).
                addQueryParameter("2", "2").
                build();

        boolean result = requestPattern.matches(request);

        Assert.assertFalse(result);
    }

    @Test
    public void matches_wrongQueryParameterValue_returnFalse() throws Exception {
        HTTPRequestPattern requestPattern = HTTPRequestPattern.newBuilder().
                addQueryParameter("1", "1").
                build();
        HTTPRequest request = HTTPRequest.newBuilder("/uri", HTTPMethod.POST).
                addQueryParameter("1", "2").
                build();

        boolean result = requestPattern.matches(request);

        Assert.assertFalse(result);
    }

    @Test
    public void matches_extraDuplicateQueryParameterInRequest_returnFalse() throws Exception {
        HTTPRequestPattern requestPattern = HTTPRequestPattern.newBuilder().
                addQueryParameter("1", "1").
                build();
        HTTPRequest request = HTTPRequest.newBuilder("/uri", HTTPMethod.POST).
                addQueryParameters("1", asList("1", "1")).
                build();

        boolean result = requestPattern.matches(request);

        Assert.assertFalse(result);
    }

    @Test
    public void matches_missingDuplicateQueryParameterInRequest_returnFalse() throws Exception {
        HTTPRequestPattern requestPattern = HTTPRequestPattern.newBuilder().
                addQueryParameters("1", asList("1", "1")).
                build();
        HTTPRequest request = HTTPRequest.newBuilder("/uri", HTTPMethod.POST).
                addQueryParameter("1", "1").
                build();

        boolean result = requestPattern.matches(request);

        Assert.assertFalse(result);
    }

    @Test
    public void matches_missingHeader_returnFalse() throws Exception {
        HTTPRequestPattern requestPattern = HTTPRequestPattern.newBuilder().
                addHeader("1", "1").
                build();
        HTTPRequest request = HTTPRequest.newBuilder("/uri", HTTPMethod.POST).
                addHeader("2", "2").
                build();

        boolean result = requestPattern.matches(request);

        Assert.assertFalse(result);
    }

    @Test
    public void matches_wrongHeaderValue_returnFalse() throws Exception {
        HTTPRequestPattern requestPattern = HTTPRequestPattern.newBuilder().
                addHeader("1", "1").
                build();
        HTTPRequest request = HTTPRequest.newBuilder("/uri", HTTPMethod.POST).
                addHeader("1", "2").
                build();

        boolean result = requestPattern.matches(request);

        Assert.assertFalse(result);
    }

    @Test
    public void equals_sameObject_returnTrue() throws Exception {
        HTTPRequestPattern requestPattern = getFilledRequestPattern();

        boolean result = requestPattern.equals(requestPattern);

        Assert.assertTrue(result);
    }

    @Test
    public void equals_equalObjects_returnTrue() throws Exception {
        HTTPRequestPattern requestPattern1 = getFilledRequestPattern();
        HTTPRequestPattern requestPattern2 = getFilledRequestPattern();

        boolean result = requestPattern1.equals(requestPattern2);

        Assert.assertTrue(result);
    }

    @Test
    public void equals_notEqualUri_returnFalse() throws Exception {
        HTTPRequestPattern requestPattern1 = HTTPRequestPattern.newBuilder().setUriPattern("/uri1").build();
        HTTPRequestPattern requestPattern2 = HTTPRequestPattern.newBuilder().setUriPattern("/uri2").build();

        boolean result = requestPattern1.equals(requestPattern2);

        Assert.assertFalse(result);
    }

    @Test
    public void equals_notEqualMethod_returnFalse() throws Exception {
        HTTPRequestPattern requestPattern1 = HTTPRequestPattern.newBuilder().addMethod(HTTPMethod.GET).build();
        HTTPRequestPattern requestPattern2 = HTTPRequestPattern.newBuilder().addMethod(HTTPMethod.POST).build();

        boolean result = requestPattern1.equals(requestPattern2);

        Assert.assertFalse(result);
    }

    @Test
    public void equals_notEqualQueryParameter_returnFalse() throws Exception {
        HTTPRequestPattern requestPattern1 = HTTPRequestPattern.newBuilder().addQueryParameter("1", "1").build();
        HTTPRequestPattern requestPattern2 = HTTPRequestPattern.newBuilder().addQueryParameter("1", "2").build();

        boolean result = requestPattern1.equals(requestPattern2);

        Assert.assertFalse(result);
    }

    @Test
    public void equals_notEqualHeader_returnFalse() throws Exception {
        HTTPRequestPattern requestPattern1 = HTTPRequestPattern.newBuilder().addHeader("1", "1").build();
        HTTPRequestPattern requestPattern2 = HTTPRequestPattern.newBuilder().addHeader("1", "2").build();

        boolean result = requestPattern1.equals(requestPattern2);

        Assert.assertFalse(result);
    }

    @Test
    public void hashCode_sameObject_equals() throws Exception {
        HTTPRequestPattern requestPattern = getFilledRequestPattern();

        Assert.assertEquals(requestPattern.hashCode(), requestPattern.hashCode());
    }

    @Test
    public void hashCode_equalObjects_equals() throws Exception {
        HTTPRequestPattern requestPattern1 = getFilledRequestPattern();
        HTTPRequestPattern requestPattern2 = getFilledRequestPattern();

        Assert.assertEquals(requestPattern1.hashCode(), requestPattern2.hashCode());
    }

    @Test
    public void hashCode_notEqualUri_notEquals() throws Exception {
        HTTPRequestPattern requestPattern1 = HTTPRequestPattern.newBuilder().setUriPattern("/uri1").build();
        HTTPRequestPattern requestPattern2 = HTTPRequestPattern.newBuilder().setUriPattern("/uri2").build();

        Assert.assertNotEquals(requestPattern1.hashCode(), requestPattern2.hashCode());
    }

    @Test
    public void hashCode_notEqualMethod_notEquals() throws Exception {
        HTTPRequestPattern requestPattern1 = HTTPRequestPattern.newBuilder().addMethod(HTTPMethod.GET).build();
        HTTPRequestPattern requestPattern2 = HTTPRequestPattern.newBuilder().addMethod(HTTPMethod.POST).build();

        Assert.assertNotEquals(requestPattern1.hashCode(), requestPattern2.hashCode());
    }

    @Test
    public void hashCode_notEqualQueryParameter_notEquals() throws Exception {
        HTTPRequestPattern requestPattern1 = HTTPRequestPattern.newBuilder().addQueryParameter("1", "1").build();
        HTTPRequestPattern requestPattern2 = HTTPRequestPattern.newBuilder().addQueryParameter("1", "2").build();

        Assert.assertNotEquals(requestPattern1.hashCode(), requestPattern2.hashCode());
    }

    @Test
    public void hashCode_notEqualHeader_notEquals() throws Exception {
        HTTPRequestPattern requestPattern1 = HTTPRequestPattern.newBuilder().addHeader("1", "1").build();
        HTTPRequestPattern requestPattern2 = HTTPRequestPattern.newBuilder().addHeader("1", "2").build();

        Assert.assertNotEquals(requestPattern1.hashCode(), requestPattern2.hashCode());
    }
}