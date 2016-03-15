package org.mb.settings.marshalling;

import org.apache.log4j.Logger;
import org.mb.http.basic.HTTPResponse;
import org.mb.http.mapping.HTTPRequestPattern;
import org.mb.http.mapping.Mapping;
import org.mb.parsing.Parsing;
import org.mb.parsing.PathType;
import org.mb.settings.Settings;

import java.util.List;
import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 27.06.2015.
 */
public final class Marshaller extends BaseMarshaller {
    private static final String MAPPING         = "mapping";
    private static final String PARSING         = "parsing";
    private static final String REQUEST         = "request";
    private static final String RESPONSE        = "response";
    private static final String URI             = "uri";
    private static final String METHOD          = "method";
    private static final String QUERY           = "query";
    private static final String HEADER          = "header";
    private static final String STATUS          = "status";
    private static final String CONTENT         = "content";
    private static final Logger log = Logger.getLogger(Marshaller.class);

    private Marshaller(Object o) {
        super(o);
    }

    public static Marshaller from(Object o) {
        return new Marshaller(o);
    }

    public Settings toSettings() throws MarshallingException {
        Map settingsMap = toType(Map.class, true);
        Mapping mapping = from(settingsMap.get(MAPPING)).toMapping();
        log.debug(String.format("Mapping is loaded:%n%s", mapping));
        Parsing parsing = from(settingsMap.get(PARSING)).toParsing();
        log.debug(String.format("Global parsing is loaded:%n%s", parsing));
        return new Settings(mapping, parsing);
    }

    public Mapping toMapping() throws MarshallingException {
        Mapping mapping = new Mapping();
        List httpMappingList = toType(List.class, true);
        for (Object httpMappingObject : httpMappingList) {
            Map httpMappingMap = from(httpMappingObject).toType(Map.class, true);
            HTTPRequestPattern pattern = from(httpMappingMap.get(REQUEST)).toRequestPattern();
            HTTPResponse response = from(httpMappingMap.get(RESPONSE)).toResponse();
            Parsing parsing = from(httpMappingMap.get(PARSING)).toParsing();
            mapping.addMapping(pattern, response, parsing);
        }
        return mapping;
    }

    public HTTPRequestPattern toRequestPattern() throws MarshallingException {
        Map httpRequestPatternMap = toType(Map.class, true);

        HTTPRequestPattern.Builder builder = HTTPRequestPattern.builder();

        Object uriObject = httpRequestPatternMap.get(URI);
        String uri = from(uriObject).toStr();
        if (!uri.isEmpty()) {
            builder.setUriRegex(uri);
        }

        Object methodObject = httpRequestPatternMap.get(METHOD);
        builder.addMethodsAsStrings(from(methodObject).toListOfType(String.class));

        Object queryParameterObject = httpRequestPatternMap.get(QUERY);
        builder.addQueryParameters(from(queryParameterObject).toMapOfType(String.class, String.class));

        Object headerObject = httpRequestPatternMap.get(HEADER);
        builder.addHeaders(from(headerObject).toMapOfType(String.class, String.class));

        Object contentObject = httpRequestPatternMap.get(CONTENT);
        builder.addContentParameters(from(contentObject).toMapOfType(String.class, String.class));

        return builder.build();
    }

    public HTTPResponse toResponse() throws MarshallingException {
        Map httpResponseMap = toType(Map.class, true);

        HTTPResponse.Builder builder = HTTPResponse.builder();

        Object statusCodeObject = httpResponseMap.get(STATUS);
        String statusCode = from(statusCodeObject).toStr();
        if (!statusCode.isEmpty()) {
            builder.setStatusCode(statusCode);
        }

        Object headerObject = httpResponseMap.get(HEADER);
        builder.addHeaders(from(headerObject).toMapOfType(String.class, String.class));

        Object contentObject = httpResponseMap.get(CONTENT);
        String content = from(contentObject).toStr();
        if (!content.isEmpty()) {
            builder.setContent(content);
        }

        return builder.build();
    }

    public Parsing toParsing() throws MarshallingException {
        Parsing parsing = new Parsing();

        Map<String, Map> map = toMapOfType(String.class, Map.class);
        for (Map.Entry<String, Map> entry : map.entrySet()) {
            PathType pathType = PathType.of(entry.getKey());
            Map<String, String> paths = from(entry.getValue()).toMapOfType(String.class, String.class);
            parsing.addPaths(pathType, paths);
        }

        return parsing;
    }
}