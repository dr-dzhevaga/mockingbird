package org.mb.http.mapping

import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.mb.http.basic.HTTPMethod
import org.mb.http.basic.HTTPRequest
import org.mb.http.basic.HTTPResponse
import org.mb.parsing.Parsing
import spock.lang.Specification

import static java.util.Arrays.asList

/**
 * Created by Dmitriy Dzhevaga on 13.03.2016.
 */
class MappingSpecification extends Specification {
    def setupSpec() {
        Logger.getLogger("org.mb").setLevel(Level.ERROR)
    }

    def "empty mapping returns default response"() {
        given:
        def mapping = new Mapping()
        def request = HTTPRequest.builder("/uri", HTTPMethod.GET).build()

        when:
        def resolvedResponse = mapping.resolve(request, [:]).getResponse()

        then:
        resolvedResponse == getDefaultResponse()
    }

    def "mapping with no matched request returns default response"() {
        given:
        def mapping = new Mapping()
        def pattern = HTTPRequestPattern.builder().addMethod(HTTPMethod.GET).build()
        def response = HTTPResponse.builder().build()
        mapping.addMapping(pattern, response, new Parsing())
        def request = HTTPRequest.builder("/uri", HTTPMethod.POST).build()

        when:
        def resolvedResponse = mapping.resolve(request, [:]).getResponse()

        then:
        resolvedResponse == getDefaultResponse()
    }

    def "mapping returns proper response"() {
        given:
        def mapping = new Mapping()
        def pattern1 = HTTPRequestPattern.builder().addMethod(HTTPMethod.GET).build()
        def pattern2 = HTTPRequestPattern.builder().addMethod(HTTPMethod.POST).build()
        def response1 = HTTPResponse.builder().setContent("1").build()
        def response2 = HTTPResponse.builder().setContent("2").build()
        mapping.addMapping(pattern1, response1, new Parsing())
        mapping.addMapping(pattern2, response2, new Parsing())
        def request = HTTPRequest.builder("/uri", HTTPMethod.POST).build()

        when:
        def resolvedResponse = mapping.resolve(request, [:]).getResponse()

        then:
        resolvedResponse == response2
    }

    def "mapping returns the first proper response"() {
        given:
        def mapping = new Mapping()
        def pattern1 = HTTPRequestPattern.builder().addMethod(HTTPMethod.GET).build()
        def pattern2 = HTTPRequestPattern.builder().addMethods(asList(HTTPMethod.GET, HTTPMethod.POST)).build()
        def pattern3 = HTTPRequestPattern.builder().addMethods(asList(HTTPMethod.GET, HTTPMethod.POST, HTTPMethod.PUT)).build()
        def response1 = HTTPResponse.builder().setContent("1").build()
        def response2 = HTTPResponse.builder().setContent("2").build()
        def response3 = HTTPResponse.builder().setContent("3").build()
        mapping.addMapping(pattern1, response1, new Parsing())
        mapping.addMapping(pattern2, response2, new Parsing())
        mapping.addMapping(pattern3, response3, new Parsing())
        def request = HTTPRequest.builder("/uri", HTTPMethod.GET).build()

        when:
        def resolvedResponse = mapping.resolve(request, [:]).getResponse()

        then:
        resolvedResponse == response1
    }

    private static def getDefaultResponse() {
        HTTPResponse.builder().setStatusCode(404).setContent("Not found").build()
    }
}