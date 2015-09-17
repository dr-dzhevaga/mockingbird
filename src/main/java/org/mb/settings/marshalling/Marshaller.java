package org.mb.settings.marshalling;

import org.mb.http.mapping.ResponseDataMapping;
import org.mb.http.mapping.RequestPattern;
import org.mb.http.basic.Response;
import org.mb.parsing.Parsing;
import org.mb.parsing.PathType;
import org.mb.settings.Settings;

import java.util.List;
import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 27.06.2015.
 */
public class Marshaller extends BaseMarshaller {
    private final static String MAPPING         = "mapping";
    private final static String PARSING         = "parsing";
    private final static String REQUEST         = "request";
    private final static String RESPONSE        = "response";
    private final static String URI             = "url";
    private final static String METHOD          = "method";
    private final static String QUERY_PARAMETER = "queryParameter";
    private final static String HEADER          = "header";
    private final static String STATUS_CODE     = "statusCode";
    private final static String CONTENT         = "content";

    private Marshaller(Object o) {
        super(o);
    }

    public static Marshaller from(Object o) {
        return new Marshaller(o);
    }

    public Settings toSettings() throws MarshallingException {
        Map settingsMap = toType(Map.class, true);
        ResponseDataMapping mapping = from(settingsMap.get(MAPPING)).toHTTPMapping();
        Parsing parsing = from(settingsMap.get(PARSING)).toParsing();
        return new Settings(mapping, parsing);
    }

    public ResponseDataMapping toHTTPMapping() throws MarshallingException {
        ResponseDataMapping responseDataMapping = new ResponseDataMapping();

        List httpMappingList = toType(List.class, true);
        for(Object httpMappingObject : httpMappingList) {
            Map httpMappingMap = from(httpMappingObject).toType(Map.class, true);
            RequestPattern requestPattern = from(httpMappingMap.get(REQUEST)).toHTTPRequestPattern();
            Response response = from(httpMappingMap.get(RESPONSE)).toHTTPResponse();
            Parsing parsing = from(httpMappingMap.get(PARSING)).toParsing();
            responseDataMapping.addMapping(requestPattern, response, parsing);
        }
        return responseDataMapping;
    }

    public RequestPattern toHTTPRequestPattern() throws MarshallingException {
        Map httpRequestPatternMap = toType(Map.class, true);

        RequestPattern.Builder builder = RequestPattern.newBuilder();

        Object uriObject = httpRequestPatternMap.get(URI);
        String uri = from(uriObject).toStr();
        if(!uri.isEmpty()) {
            builder.setUriPattern(uri);
        }

        Object methodObject = httpRequestPatternMap.get(METHOD);
        builder.addMethodsAsStrings(from(methodObject).toListOfType(String.class));

        Object queryParameterObject = httpRequestPatternMap.get(QUERY_PARAMETER);
        builder.addQueryParameters(from(queryParameterObject).toMultimapOfType(String.class, String.class));

        Object headerObject = httpRequestPatternMap.get(HEADER);
        builder.addHeaders(from(headerObject).toMultimapOfType(String.class, String.class));

        Object contentObject = httpRequestPatternMap.get(CONTENT);
        builder.addContentParameters(from(contentObject).toMultimapOfType(String.class, String.class));

        return builder.build();
    }

    public Response toHTTPResponse() throws MarshallingException {
        Map httpResponseMap = toType(Map.class, true);

        Response.Builder builder = Response.newBuilder();

        Object statusCodeObject = httpResponseMap.get(STATUS_CODE);
        String statusCode = from(statusCodeObject).toStr();
        if(!statusCode.isEmpty()) {
            builder.setStatusCode(statusCode);
        }

        Object headerObject = httpResponseMap.get(HEADER);
        builder.addHeaders(from(headerObject).toMapOfType(String.class, String.class));

        Object contentObject = httpResponseMap.get(CONTENT);
        String content = from(contentObject).toStr();
        if(!content.isEmpty()) {
            builder.setContent(content);
        }

        return builder.build();
    }

    public Parsing toParsing() throws MarshallingException {
        Parsing parsing = new Parsing();

        Map<String, Map> map = toMapOfType(String.class, Map.class);
        for(Map.Entry<String, Map> entry : map.entrySet()) {
            PathType pathType = PathType.of(entry.getKey());
            Map<String, String> paths = from(entry.getValue()).toMapOfType(String.class, String.class);
            parsing.addParsing(pathType, paths);
        }

        return parsing;
    }
}