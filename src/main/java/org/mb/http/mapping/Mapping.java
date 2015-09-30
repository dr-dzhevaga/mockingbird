package org.mb.http.mapping;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.mb.http.basic.Request;
import org.mb.http.basic.Response;
import org.mb.parsing.Parsing;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 19.06.2015.
 */
public class Mapping {
    private static final int    DEFAULT_RESPONSE_STATUS_CODE  = 404;
    private static final String DEFAULT_RESPONSE_CONTENT      = "Not found";
    private static final String LOG_MAPPING_IS_ADDED          = "Mapping is added\n" +
                                                                "Request pattern:\n" +
                                                                "%s\n" +
                                                                "Response:\n" +
                                                                "%s\n" +
                                                                "Request parsing: %s";
    private static final String LOG_RESPONSE_IS_FOUND         = "Response is found in mapping";
    private static final String LOG_RESPONSE_IS_NOT_FOUND     = "Response is not found in mapping, default response will be used";
    private static final Logger Log = Logger.getLogger(Mapping.class);

    private List<MappingEntry> mapping = Lists.newArrayList();
    private final MappingEntry defaultMappingEntry;

    public Mapping() {
        Response defaultResponse = Response.newBuilder().
                setStatusCode(DEFAULT_RESPONSE_STATUS_CODE).
                setContent(DEFAULT_RESPONSE_CONTENT).
                build();
        RequestPattern requestPattern = RequestPattern.newBuilder().build();
        Parsing parsing = new Parsing();
        defaultMappingEntry = new MappingEntry(requestPattern, defaultResponse, parsing);
    }

    public void addMapping(RequestPattern requestPattern, Response response, Parsing parsing) {
        mapping.add(new MappingEntry(requestPattern, response, parsing));
        Log.info(String.format(LOG_MAPPING_IS_ADDED, requestPattern, response, parsing));
    }

    public MappingEntry resolve(Request request, Map<String, String> content) {
        for(MappingEntry mappingEntry : mapping) {
            if(mappingEntry.getRequestPattern().matches(request, content)) {
                Log.info(LOG_RESPONSE_IS_FOUND);
                return mappingEntry;
            }
        }
        Log.info(LOG_RESPONSE_IS_NOT_FOUND);
        return defaultMappingEntry;
    }

    public static class MappingEntry {
        private final RequestPattern requestPattern;
        private final Response response;
        private final Parsing parsing;

        private MappingEntry(RequestPattern requestPattern, Response response, Parsing parsing) {
            this.requestPattern = requestPattern;
            this.response = response;
            this.parsing = parsing;
        }

        public Parsing getParsing() {
            return parsing;
        }

        public Response getResponse() {
            return response;
        }

        private RequestPattern getRequestPattern() {
            return requestPattern;
        }
    }
}