package org.mb.http.mapping;

import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.mb.http.basic.Request;
import org.mb.http.basic.Response;
import org.mb.parsing.BulkParser;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 19.06.2015.
 */
public class HandlerDataMapping {
    private static final int    DEFAULT_RESPONSE_STATUS_CODE  = 404;
    private static final String DEFAULT_RESPONSE_CONTENT      = "Not found";

    private static final String LOG_MAPPING_IS_ADDED        = "Mapping is added%nRequest pattern:%n%s%nResponse:%n%s";
    private static final String LOG_RESPONSE_IS_FOUND       = "Response is found in mapping";
    private static final String LOG_RESPONSE_IS_NOT_FOUND   = "Response is not found in mapping, default response will be used";

    final static private Logger Log = Logger.getLogger(HandlerDataMapping.class);
    private LinkedHashMap<RequestPattern, HandlerData> mapping = Maps.newLinkedHashMap();
    private final HandlerData defaultHandlerData;

    public HandlerDataMapping() {
        Response defaultResponse = Response.newBuilder().
                setStatusCode(DEFAULT_RESPONSE_STATUS_CODE).
                setContent(DEFAULT_RESPONSE_CONTENT).
                build();
        defaultHandlerData = new HandlerData(defaultResponse, new BulkParser());
    }

    public void addMapping(RequestPattern requestPattern, Response response, BulkParser bulkParser) {
        mapping.put(requestPattern, new HandlerData(response, bulkParser));
        Log.info(String.format(LOG_MAPPING_IS_ADDED, requestPattern, response));
    }

    public HandlerData find(Request request, Map<String, String> content) {
        for(Map.Entry<RequestPattern, HandlerData> mappingEntry : mapping.entrySet()) {
            if(mappingEntry.getKey().matches(request, content)) {
                Log.info(LOG_RESPONSE_IS_FOUND);
                return mappingEntry.getValue();
            }
        }
        Log.info(LOG_RESPONSE_IS_NOT_FOUND);
        return defaultHandlerData;
    }

    public static class HandlerData {
        private final Response response;
        private final BulkParser bulkParser;

        private HandlerData(Response response, BulkParser bulkParser) {
            this.response = response;
            this.bulkParser = bulkParser;
        }

        public BulkParser getBulkParser() {
            return bulkParser;
        }

        public Response getResponse() {
            return response;
        }
    }
}