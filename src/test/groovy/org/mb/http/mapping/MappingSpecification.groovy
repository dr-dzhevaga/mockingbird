package org.mb.http.mapping

import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.mb.http.basic.Method
import org.mb.http.basic.Request
import org.mb.http.basic.Response
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
        def request = Request.newBuilder("/uri", Method.GET).build()

        when:
        def resolvedResponse = mapping.resolve(request, [:]).getResponse()

        then:
        resolvedResponse == getDefaultResponse()
    }

    def "mapping with no matched request returns default response"() {
        given:
        def mapping = new Mapping()
        def pattern = RequestPattern.newBuilder().addMethod(Method.GET).build()
        def response = Response.newBuilder().build()
        mapping.addMapping(pattern, response, new Parsing())
        def request = Request.newBuilder("/uri", Method.POST).build()

        when:
        def resolvedResponse = mapping.resolve(request, [:]).getResponse()

        then:
        resolvedResponse == getDefaultResponse()
    }

    def "mapping returns proper response"() {
        given:
        def mapping = new Mapping()
        def pattern1 = RequestPattern.newBuilder().addMethod(Method.GET).build()
        def pattern2 = RequestPattern.newBuilder().addMethod(Method.POST).build()
        def response1 = Response.newBuilder().setContent("1").build()
        def response2 = Response.newBuilder().setContent("2").build()
        mapping.addMapping(pattern1, response1, new Parsing())
        mapping.addMapping(pattern2, response2, new Parsing())
        def request = Request.newBuilder("/uri", Method.POST).build()

        when:
        def resolvedResponse = mapping.resolve(request, [:]).getResponse()

        then:
        resolvedResponse == response2
    }

    def "mapping returns the first proper response"() {
        given:
        def mapping = new Mapping()
        def pattern1 = RequestPattern.newBuilder().addMethod(Method.GET).build()
        def pattern2 = RequestPattern.newBuilder().addMethods(asList(Method.GET, Method.POST)).build()
        def pattern3 = RequestPattern.newBuilder().addMethods(asList(Method.GET, Method.POST, Method.PUT)).build()
        def response1 = Response.newBuilder().setContent("1").build()
        def response2 = Response.newBuilder().setContent("2").build()
        def response3 = Response.newBuilder().setContent("3").build()
        mapping.addMapping(pattern1, response1, new Parsing())
        mapping.addMapping(pattern2, response2, new Parsing())
        mapping.addMapping(pattern3, response3, new Parsing())
        def request = Request.newBuilder("/uri", Method.GET).build()

        when:
        def resolvedResponse = mapping.resolve(request, [:]).getResponse()

        then:
        resolvedResponse == response1
    }

    private static def getDefaultResponse() {
        Response.newBuilder().setStatusCode(404).setContent("Not found").build()
    }
}