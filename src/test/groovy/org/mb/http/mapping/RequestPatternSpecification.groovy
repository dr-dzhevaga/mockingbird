package org.mb.http.mapping

import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.mb.http.basic.Method
import org.mb.http.basic.Request
import spock.lang.Specification

import static java.util.Arrays.asList

/**
 * Created by Dmitriy Dzhevaga on 13.03.2016.
 */
class RequestPatternSpecification extends Specification {
    def setupSpec() {
        Logger.getLogger("org.mb").setLevel(Level.ERROR)
    }

    def "empty pattern matches"() {
        given:
        def pattern = RequestPattern.newBuilder().build();
        def request = Request.newBuilder("/uri", Method.GET).build();
        def content = ["1": "1"]

        expect:
        pattern.matches(request, content)
    }

    def "pattern with the same parameters matches"() {
        given:
        def pattern = RequestPattern.newBuilder().
                setUriRegex("/uri").
                addMethod(Method.GET).
                addQueryParameter("1", "1").
                addHeader("1", "1").
                addContentParameter("1", "1").
                build()
        def request = Request.newBuilder("/uri", Method.GET).
                addQueryParameter("1", "1").
                addHeader("1", "1").
                build()
        def content = ["1": "1"]

        expect:
        pattern.matches(request, content)
    }

    def "pattern with wider parameters restrictions matches"() {
        given:
        def pattern = RequestPattern.newBuilder().
                setUriRegex("/uri/.*").
                addMethods(asList(Method.GET, Method.POST)).
                addQueryParameter("1", "\\d").
                addHeader("1", "\\d").
                addContentParameter("1", "\\d").
                build()
        def request = Request.newBuilder("/uri/uri", Method.GET).
                addQueryParameter("1", "1").
                addHeader("1", "1").
                setContent("content").
                build()
        def content = ["1": "1"]

        expect:
        pattern.matches(request, content)
    }

    def "request with wrong uri is not matched"() {
        given:
        def pattern = RequestPattern.newBuilder().setUriRegex("/uri/uri").build()
        def request = Request.newBuilder("/uri", Method.GET).build()

        expect:
        !pattern.matches(request, [:])
    }

    def "request with wrong method is not matched"() {
        given:
        def pattern = RequestPattern.newBuilder().addMethod(Method.GET).build()
        def request = Request.newBuilder("/uri", Method.POST).build()

        expect:
        !pattern.matches(request, [:])
    }

    def "request with missing query parameter is not matched"() {
        given:
        def pattern = RequestPattern.newBuilder().
                addQueryParameter("1", "1").
                addQueryParameter("2", "2").
                build()
        def request = Request.newBuilder("/uri", Method.GET).
                addQueryParameter("1", "1").
                build()

        expect:
        !pattern.matches(request, [:])
    }

    def "request with wrong query parameter is not matched"() {
        given:
        def pattern = RequestPattern.newBuilder().
                addQueryParameter("1", "\\s").
                build()
        def request = Request.newBuilder("/uri", Method.GET).
                addQueryParameter("1", "1").
                build()

        expect:
        !pattern.matches(request, [:])
    }

    def "request with missing header parameter is not matched"() {
        given:
        def pattern = RequestPattern.newBuilder().
                addHeader("1", "1").
                addHeader("2", "2").
                build()
        def request = Request.newBuilder("/uri", Method.GET).
                addHeader("1", "1").
                build()

        expect:
        !pattern.matches(request, [:])
    }

    def "request with wrong header parameter is not matched"() {
        given:
        def pattern = RequestPattern.newBuilder().
                addHeader("1", "\\s").
                build()
        def request = Request.newBuilder("/uri", Method.GET).
                addHeader("1", "1").
                build()

        expect:
        !pattern.matches(request, [:])
    }

    def "content with missing parameter is not matched"() {
        given:
        def pattern = RequestPattern.newBuilder().
                addContentParameter("1", "1").
                addContentParameter("2", "2").
                build()
        def request = Request.newBuilder("/uri", Method.GET).build()
        def content = ["1": "1"]

        expect:
        !pattern.matches(request, content)
    }

    def "content with wrong parameter value is not matched"() {
        given:
        def pattern = RequestPattern.newBuilder().
                addContentParameter("1", "\\s").
                build()
        def request = Request.newBuilder("/uri", Method.GET).build()
        def content = ["1": "1"]

        expect:
        !pattern.matches(request, content)
    }

    def "the same patterns are equal and have equal hashcode"() {
        given:
        def pattern = RequestPattern.newBuilder().build()
        expect:
        pattern.equals(pattern)
        pattern.hashCode() == pattern.hashCode()
    }

    def "equal patterns are equal and have equal hashcode"() {
        given:
        def pattern1 = RequestPattern.newBuilder().build()
        def pattern2 = RequestPattern.newBuilder().build()

        expect:
        pattern1.equals(pattern2)
        pattern1.hashCode() == pattern2.hashCode()
    }

    def "patterns with different uri are not equal and have different hashcodes"() {
        given:
        def pattern1 = RequestPattern.newBuilder().setUriRegex("/uri1").build()
        def pattern2 = RequestPattern.newBuilder().setUriRegex("/uri2").build()
        expect:
        !pattern1.equals(pattern2)
        pattern1.hashCode() != pattern2.hashCode()
    }

    def "patterns with different methods are not equal and have different hashcodes"() {
        given:
        def pattern1 = RequestPattern.newBuilder().addMethod(Method.GET).build()
        def pattern2 = RequestPattern.newBuilder().addMethod(Method.POST).build()

        expect:
        !pattern1.equals(pattern2)
        pattern1.hashCode() != pattern2.hashCode()
    }

    def "patterns with different query parameters are not equal and have different hashcodes"() {
        given:
        def pattern1 = RequestPattern.newBuilder().addQueryParameter("1", "1").build()
        def pattern2 = RequestPattern.newBuilder().addQueryParameter("1", "2").build()

        expect:
        !pattern1.equals(pattern2)
        pattern1.hashCode() != pattern2.hashCode()
    }

    def "patterns with different headers are not equal and have different hashcodes"() {
        given:
        def pattern1 = RequestPattern.newBuilder().addHeader("1", "1").build()
        def pattern2 = RequestPattern.newBuilder().addHeader("1", "2").build()
        expect:
        !pattern1.equals(pattern2)
        pattern1.hashCode() != pattern2.hashCode()
    }

    def "patterns with different content parameters are not equal and have different hashcodes"() {
        given:
        def pattern1 = RequestPattern.newBuilder().addContentParameter("1", "1").build()
        def pattern2 = RequestPattern.newBuilder().addContentParameter("1", "2").build()

        expect:
        !pattern1.equals(pattern2)
        pattern1.hashCode() != pattern2.hashCode()
    }
}