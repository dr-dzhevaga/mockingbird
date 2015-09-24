package org.mb.http.mapping;

import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.mb.http.basic.Request;
import org.mb.http.basic.Response;
import org.mb.parsing.Parsing;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 19.06.2015.
 */
public class ResponseDataMapping {
    private static final int    DEFAULT_RESPONSE_STATUS_CODE  = 404;
    private static final String DEFAULT_RESPONSE_CONTENT      = "Not found";
    private static final String LOG_MAPPING_IS_ADDED          = "Mapping is added\n" +
                                                                "*Request pattern:\n" +
                                                                "%s\n" +
                                                                "*Response:\n" +
                                                                "%s%n" +
                                                                "*Parsing:\n" +
                                                                "%s%n";
    private static final String LOG_RESPONSE_IS_FOUND         = "Response is found in mapping";
    private static final String LOG_RESPONSE_IS_NOT_FOUND     = "Response is not found in mapping, default response will be used";
    private static final Logger Log = Logger.getLogger(ResponseDataMapping.class);

    private LinkedHashMap<RequestPattern, ResponseData> mapping = Maps.newLinkedHashMap();
    private final ResponseData defaultResponseData;

    public ResponseDataMapping() {
        Response defaultResponse = Response.newBuilder().
                setStatusCode(DEFAULT_RESPONSE_STATUS_CODE).
                setContent(DEFAULT_RESPONSE_CONTENT).
                build();
        defaultResponseData = new ResponseData(defaultResponse, new Parsing());
    }

    public void addMapping(RequestPattern requestPattern, Response response, Parsing parsing) {
        mapping.put(requestPattern, new ResponseData(response, parsing));
        Log.info(String.format(LOG_MAPPING_IS_ADDED, requestPattern, response, parsing));
    }

    public ResponseData find(Request request, Map<String, String> content) {
        for(Map.Entry<RequestPattern, ResponseData> mappingEntry : mapping.entrySet()) {
            if(mappingEntry.getKey().matches(request, content)) {
                Log.info(LOG_RESPONSE_IS_FOUND);
                return mappingEntry.getValue();
            }
        }
        Log.info(LOG_RESPONSE_IS_NOT_FOUND);
        return defaultResponseData;
    }

    public static class ResponseData {
        private final Response response;
        private final Parsing parsing;

        private ResponseData(Response response, Parsing parsing) {
            this.response = response;
            this.parsing = parsing;
        }

        public Parsing getParsing() {
            return parsing;
        }

        public Response getResponse() {
            return response;
        }
    }
}