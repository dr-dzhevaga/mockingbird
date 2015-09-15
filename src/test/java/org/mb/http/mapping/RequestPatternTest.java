package org.mb.http.mapping;

import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Test;
import org.mb.http.basic.Method;
import org.mb.http.basic.Request;

import java.util.Map;

import static java.util.Arrays.asList;

/**
 * Created by Dmitriy Dzhevaga on 12.07.2015.
 */
// TODO: test content matching
public class RequestPatternTest {

    private RequestPattern getEmptyRequestPattern() {
        return RequestPattern.newBuilder().build();
    }

    private RequestPattern getFilledRequestPattern() {
        return RequestPattern.newBuilder().
                setUriPattern("/uri").
                addMethod(Method.GET).
                addQueryParameter("1", "1").
                addHeader("1", "1").
                build();
    }

    private Map<String, String> getContent() {
        return Maps.newHashMap();
    }

    @Test
    public void matches_defaultPattern_returnTrue() throws Exception {
        RequestPattern requestPattern = getEmptyRequestPattern();
        Request request = Request.newBuilder("/uri", Method.GET).build();

        boolean result = requestPattern.matches(request, getContent());

        Assert.assertTrue(result);
    }

    @Test
    public void matches_requestEqualsPattern_returnTrue() throws Exception {
        RequestPattern requestPattern = getFilledRequestPattern();
        Request request = Request.newBuilder("/uri", Method.GET).
                addQueryParameter("1", "1").
                addHeader("1", "1").
                setContent("content").
                build();

        boolean result = requestPattern.matches(request, getContent());

        Assert.assertTrue(result);
    }

    @Test
    public void matches_patternContainsRequest_returnTrue() throws Exception {
        RequestPattern requestPattern = RequestPattern.newBuilder().
                setUriPattern("/uri/.*").
                addMethods(asList(Method.GET, Method.POST)).
                addHeaders("1", asList("1", "2")).
                build();
        Request request = Request.newBuilder("/uri/uri", Method.GET).
                addHeader("1", "1").
                setContent("content").
                build();

        boolean result = requestPattern.matches(request, getContent());

        Assert.assertTrue(result);
    }

    @Test
    public void matches_wrongUri_returnFalse() throws Exception {
        RequestPattern requestPattern = RequestPattern.newBuilder().
                setUriPattern("/uri/uri").
                build();
        Request request = Request.newBuilder("/uri", Method.GET).build();

        boolean result = requestPattern.matches(request, getContent());

        Assert.assertFalse(result);
    }

    @Test
    public void matches_wrongMethod_returnFalse() throws Exception {
        RequestPattern requestPattern = RequestPattern.newBuilder().
                addMethod(Method.GET).
                build();
        Request request = Request.newBuilder("/uri", Method.POST).build();

        boolean result = requestPattern.matches(request, getContent());

        Assert.assertFalse(result);
    }

    @Test
    public void matches_missingQueryParameter_returnFalse() throws Exception {
        RequestPattern requestPattern = RequestPattern.newBuilder().
                addQueryParameter("1", "1").
                build();
        Request request = Request.newBuilder("/uri", Method.POST).
                addQueryParameter("2", "2").
                build();

        boolean result = requestPattern.matches(request, getContent());

        Assert.assertFalse(result);
    }

    @Test
    public void matches_wrongQueryParameterValue_returnFalse() throws Exception {
        RequestPattern requestPattern = RequestPattern.newBuilder().
                addQueryParameter("1", "1").
                build();
        Request request = Request.newBuilder("/uri", Method.POST).
                addQueryParameter("1", "2").
                build();

        boolean result = requestPattern.matches(request, getContent());

        Assert.assertFalse(result);
    }

    @Test
    public void matches_extraDuplicateQueryParameterInRequest_returnFalse() throws Exception {
        RequestPattern requestPattern = RequestPattern.newBuilder().
                addQueryParameter("1", "1").
                build();
        Request request = Request.newBuilder("/uri", Method.POST).
                addQueryParameters("1", asList("1", "1")).
                build();

        boolean result = requestPattern.matches(request, getContent());

        Assert.assertFalse(result);
    }

    @Test
    public void matches_missingDuplicateQueryParameterInRequest_returnFalse() throws Exception {
        RequestPattern requestPattern = RequestPattern.newBuilder().
                addQueryParameters("1", asList("1", "1")).
                build();
        Request request = Request.newBuilder("/uri", Method.POST).
                addQueryParameter("1", "1").
                build();

        boolean result = requestPattern.matches(request, getContent());

        Assert.assertFalse(result);
    }

    @Test
    public void matches_missingHeader_returnFalse() throws Exception {
        RequestPattern requestPattern = RequestPattern.newBuilder().
                addHeader("1", "1").
                build();
        Request request = Request.newBuilder("/uri", Method.POST).
                addHeader("2", "2").
                build();

        boolean result = requestPattern.matches(request, getContent());

        Assert.assertFalse(result);
    }

    @Test
    public void matches_wrongHeaderValue_returnFalse() throws Exception {
        RequestPattern requestPattern = RequestPattern.newBuilder().
                addHeader("1", "1").
                build();
        Request request = Request.newBuilder("/uri", Method.POST).
                addHeader("1", "2").
                build();

        boolean result = requestPattern.matches(request, getContent());

        Assert.assertFalse(result);
    }

    @Test
    public void equals_sameObject_returnTrue() throws Exception {
        RequestPattern requestPattern = getFilledRequestPattern();

        boolean result = requestPattern.equals(requestPattern);

        Assert.assertTrue(result);
    }

    @Test
    public void equals_equalObjects_returnTrue() throws Exception {
        RequestPattern requestPattern1 = getFilledRequestPattern();
        RequestPattern requestPattern2 = getFilledRequestPattern();

        boolean result = requestPattern1.equals(requestPattern2);

        Assert.assertTrue(result);
    }

    @Test
    public void equals_notEqualUri_returnFalse() throws Exception {
        RequestPattern requestPattern1 = RequestPattern.newBuilder().setUriPattern("/uri1").build();
        RequestPattern requestPattern2 = RequestPattern.newBuilder().setUriPattern("/uri2").build();

        boolean result = requestPattern1.equals(requestPattern2);

        Assert.assertFalse(result);
    }

    @Test
    public void equals_notEqualMethod_returnFalse() throws Exception {
        RequestPattern requestPattern1 = RequestPattern.newBuilder().addMethod(Method.GET).build();
        RequestPattern requestPattern2 = RequestPattern.newBuilder().addMethod(Method.POST).build();

        boolean result = requestPattern1.equals(requestPattern2);

        Assert.assertFalse(result);
    }

    @Test
    public void equals_notEqualQueryParameter_returnFalse() throws Exception {
        RequestPattern requestPattern1 = RequestPattern.newBuilder().addQueryParameter("1", "1").build();
        RequestPattern requestPattern2 = RequestPattern.newBuilder().addQueryParameter("1", "2").build();

        boolean result = requestPattern1.equals(requestPattern2);

        Assert.assertFalse(result);
    }

    @Test
    public void equals_notEqualHeader_returnFalse() throws Exception {
        RequestPattern requestPattern1 = RequestPattern.newBuilder().addHeader("1", "1").build();
        RequestPattern requestPattern2 = RequestPattern.newBuilder().addHeader("1", "2").build();

        boolean result = requestPattern1.equals(requestPattern2);

        Assert.assertFalse(result);
    }

    @Test
    public void hashCode_sameObject_equals() throws Exception {
        RequestPattern requestPattern = getFilledRequestPattern();

        Assert.assertEquals(requestPattern.hashCode(), requestPattern.hashCode());
    }

    @Test
    public void hashCode_equalObjects_equals() throws Exception {
        RequestPattern requestPattern1 = getFilledRequestPattern();
        RequestPattern requestPattern2 = getFilledRequestPattern();

        Assert.assertEquals(requestPattern1.hashCode(), requestPattern2.hashCode());
    }

    @Test
    public void hashCode_notEqualUri_notEquals() throws Exception {
        RequestPattern requestPattern1 = RequestPattern.newBuilder().setUriPattern("/uri1").build();
        RequestPattern requestPattern2 = RequestPattern.newBuilder().setUriPattern("/uri2").build();

        Assert.assertNotEquals(requestPattern1.hashCode(), requestPattern2.hashCode());
    }

    @Test
    public void hashCode_notEqualMethod_notEquals() throws Exception {
        RequestPattern requestPattern1 = RequestPattern.newBuilder().addMethod(Method.GET).build();
        RequestPattern requestPattern2 = RequestPattern.newBuilder().addMethod(Method.POST).build();

        Assert.assertNotEquals(requestPattern1.hashCode(), requestPattern2.hashCode());
    }

    @Test
    public void hashCode_notEqualQueryParameter_notEquals() throws Exception {
        RequestPattern requestPattern1 = RequestPattern.newBuilder().addQueryParameter("1", "1").build();
        RequestPattern requestPattern2 = RequestPattern.newBuilder().addQueryParameter("1", "2").build();

        Assert.assertNotEquals(requestPattern1.hashCode(), requestPattern2.hashCode());
    }

    @Test
    public void hashCode_notEqualHeader_notEquals() throws Exception {
        RequestPattern requestPattern1 = RequestPattern.newBuilder().addHeader("1", "1").build();
        RequestPattern requestPattern2 = RequestPattern.newBuilder().addHeader("1", "2").build();

        Assert.assertNotEquals(requestPattern1.hashCode(), requestPattern2.hashCode());
    }
}