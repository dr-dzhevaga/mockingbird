package org.mb.http.mapping

import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.mb.http.basic.HTTPMethod
import org.mb.http.basic.HTTPRequest
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
        def pattern = HTTPRequestPattern.builder().build();
        def request = HTTPRequest.builder("/uri", HTTPMethod.GET).build();
        def content = ["1": "1"]

        expect:
        pattern.matches(request, content)
    }

    def "pattern with the same parameters matches"() {
        given:
        def pattern = HTTPRequestPattern.builder().
                setUriRegex("/uri").
                addMethod(HTTPMethod.GET).
                addQueryParameter("1", "1").
                addHeader("1", "1").
                addContentParameter("1", "1").
                build()
        def request = HTTPRequest.builder("/uri", HTTPMethod.GET).
                addQueryParameter("1", "1").
                addHeader("1", "1").
                build()
        def content = ["1": "1"]

        expect:
        pattern.matches(request, content)
    }

    def "pattern with wider parameters restrictions matches"() {
        given:
        def pattern = HTTPRequestPattern.builder().
                setUriRegex("/uri/.*").
                addMethods(asList(HTTPMethod.GET, HTTPMethod.POST)).
                addQueryParameter("1", "\\d").
                addHeader("1", "\\d").
                addContentParameter("1", "\\d").
                build()
        def request = HTTPRequest.builder("/uri/uri", HTTPMethod.GET).
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
        def pattern = HTTPRequestPattern.builder().setUriRegex("/uri/uri").build()
        def request = HTTPRequest.builder("/uri", HTTPMethod.GET).build()

        expect:
        !pattern.matches(request, [:])
    }

    def "request with wrong method is not matched"() {
        given:
        def pattern = HTTPRequestPattern.builder().addMethod(HTTPMethod.GET).build()
        def request = HTTPRequest.builder("/uri", HTTPMethod.POST).build()

        expect:
        !pattern.matches(request, [:])
    }

    def "request with missing query parameter is not matched"() {
        given:
        def pattern = HTTPRequestPattern.builder().
                addQueryParameter("1", "1").
                addQueryParameter("2", "2").
                build()
        def request = HTTPRequest.builder("/uri", HTTPMethod.GET).
                addQueryParameter("1", "1").
                build()

        expect:
        !pattern.matches(request, [:])
    }

    def "request with wrong query parameter is not matched"() {
        given:
        def pattern = HTTPRequestPattern.builder().
                addQueryParameter("1", "\\s").
                build()
        def request = HTTPRequest.builder("/uri", HTTPMethod.GET).
                addQueryParameter("1", "1").
                build()

        expect:
        !pattern.matches(request, [:])
    }

    def "request with missing header parameter is not matched"() {
        given:
        def pattern = HTTPRequestPattern.builder().
                addHeader("1", "1").
                addHeader("2", "2").
                build()
        def request = HTTPRequest.builder("/uri", HTTPMethod.GET).
                addHeader("1", "1").
                build()

        expect:
        !pattern.matches(request, [:])
    }

    def "request with wrong header parameter is not matched"() {
        given:
        def pattern = HTTPRequestPattern.builder().
                addHeader("1", "\\s").
                build()
        def request = HTTPRequest.builder("/uri", HTTPMethod.GET).
                addHeader("1", "1").
                build()

        expect:
        !pattern.matches(request, [:])
    }

    def "content with missing parameter is not matched"() {
        given:
        def pattern = HTTPRequestPattern.builder().
                addContentParameter("1", "1").
                addContentParameter("2", "2").
                build()
        def request = HTTPRequest.builder("/uri", HTTPMethod.GET).build()
        def content = ["1": "1"]

        expect:
        !pattern.matches(request, content)
    }

    def "content with wrong parameter value is not matched"() {
        given:
        def pattern = HTTPRequestPattern.builder().
                addContentParameter("1", "\\s").
                build()
        def request = HTTPRequest.builder("/uri", HTTPMethod.GET).build()
        def content = ["1": "1"]

        expect:
        !pattern.matches(request, content)
    }

    def "the same patterns are equal and have equal hashcode"() {
        given:
        def pattern = HTTPRequestPattern.builder().build()
        expect:
        pattern.equals(pattern)
        pattern.hashCode() == pattern.hashCode()
    }

    def "equal patterns are equal and have equal hashcode"() {
        given:
        def pattern1 = HTTPRequestPattern.builder().build()
        def pattern2 = HTTPRequestPattern.builder().build()

        expect:
        pattern1.equals(pattern2)
        pattern1.hashCode() == pattern2.hashCode()
    }

    def "patterns with different uri are not equal and have different hashcodes"() {
        given:
        def pattern1 = HTTPRequestPattern.builder().setUriRegex("/uri1").build()
        def pattern2 = HTTPRequestPattern.builder().setUriRegex("/uri2").build()
        expect:
        !pattern1.equals(pattern2)
        pattern1.hashCode() != pattern2.hashCode()
    }

    def "patterns with different methods are not equal and have different hashcodes"() {
        given:
        def pattern1 = HTTPRequestPattern.builder().addMethod(HTTPMethod.GET).build()
        def pattern2 = HTTPRequestPattern.builder().addMethod(HTTPMethod.POST).build()

        expect:
        !pattern1.equals(pattern2)
        pattern1.hashCode() != pattern2.hashCode()
    }

    def "patterns with different query parameters are not equal and have different hashcodes"() {
        given:
        def pattern1 = HTTPRequestPattern.builder().addQueryParameter("1", "1").build()
        def pattern2 = HTTPRequestPattern.builder().addQueryParameter("1", "2").build()

        expect:
        !pattern1.equals(pattern2)
        pattern1.hashCode() != pattern2.hashCode()
    }

    def "patterns with different headers are not equal and have different hashcodes"() {
        given:
        def pattern1 = HTTPRequestPattern.builder().addHeader("1", "1").build()
        def pattern2 = HTTPRequestPattern.builder().addHeader("1", "2").build()
        expect:
        !pattern1.equals(pattern2)
        pattern1.hashCode() != pattern2.hashCode()
    }

    def "patterns with different content parameters are not equal and have different hashcodes"() {
        given:
        def pattern1 = HTTPRequestPattern.builder().addContentParameter("1", "1").build()
        def pattern2 = HTTPRequestPattern.builder().addContentParameter("1", "2").build()

        expect:
        !pattern1.equals(pattern2)
        pattern1.hashCode() != pattern2.hashCode()
    }
}