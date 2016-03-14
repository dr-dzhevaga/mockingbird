package org.mb.jspl

import org.apache.log4j.Level
import org.apache.log4j.Logger
import spock.lang.Specification

/**
 * Created by Dmitriy Dzhevaga on 13.03.2016.
 */
class JSPLikeProcessorSpecification extends Specification {
    def setupSpec() {
        Logger.getLogger("org.mb").setLevel(Level.ERROR)
    }

    def ""(String script, String result) {
        given:
        def reader = new StringReader(script)
        def writer = new StringWriter()
        def jspLikeProcessor = new JSPLikeProcessor(reader, writer)

        expect:
        jspLikeProcessor.print()
        writer.toString() == result

        where:
        script                                       | result
        "test"                                       | "test"
        "test\ntest\r\ntest"                         | "test\ntest\r\ntest"
        "' '' \" \"\" \\ \\\\"                       | "' '' \" \"\" \\ \\\\"
        "тест"                                       | "тест"
        "<%if(true)%>test"                           | "test"
        "te<%if(true)%>st"                           | "test"
        "test<%if(true){}%>"                         | "test"
        "<%if(true){%>test<%}%>"                     | "test"
        "<%='test'%>"                                | "test"
        "te<%='st'%>"                                | "test"
        "t<%='es'%>t"                                | "test"
        "<%='te'%>st"                                | "test"
        "<%='te'%><%='st'%>"                         | "test"
        "<%s = 'tes';%><%=s%>t"                      | "test"
        "tes<%s = 't';%><%=s%>"                      | "test"
        "t<%s = 'es';%><%=s%>t"                      | "test"
        "<%s='test'%><%for(i in s){%><%=s[i]%><%}%>" | "test"
    }
}