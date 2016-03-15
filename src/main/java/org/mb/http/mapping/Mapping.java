package org.mb.http.mapping;

import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.mb.http.basic.HTTPRequest;
import org.mb.http.basic.HTTPResponse;
import org.mb.parsing.Parsing;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Dmitriy Dzhevaga on 19.06.2015.
 */
public final class Mapping {
    private static final int DEFAULT_RESPONSE_STATUS_CODE = 404;
    private static final String DEFAULT_RESPONSE_CONTENT = "Not found";
    private static final Logger log = Logger.getLogger(Mapping.class);
    private static final Entry defaultEntry = createDefaultEntry();
    private List<Entry> mapping = Lists.newArrayList();

    public Mapping() {
    }

    private static Entry createDefaultEntry() {
        HTTPResponse response = HTTPResponse.builder().
                setStatusCode(DEFAULT_RESPONSE_STATUS_CODE).
                setContent(DEFAULT_RESPONSE_CONTENT).
                build();
        HTTPRequestPattern pattern = HTTPRequestPattern.builder().build();
        Parsing parsing = new Parsing();
        return new Entry(pattern, response, parsing);
    }

    public void addMapping(HTTPRequestPattern pattern, HTTPResponse response, Parsing parsing) {
        mapping.add(new Entry(pattern, response, parsing));
    }

    public Entry resolve(HTTPRequest request, Map<String, String> parsedContent) {
        Optional<Entry> mappingEntry = mapping.stream().filter(mapping ->
                mapping.getPattern().matches(request, parsedContent)).findFirst();
        if (mappingEntry.isPresent()) {
            log.debug("Request is found in mapping");
        } else {
            log.debug("Request is not found in mapping, default response will be used");
        }
        return mappingEntry.orElse(defaultEntry);
    }

    @Override
    public String toString() {
        return mapping.stream().map(Entry::toString).collect(Collectors.joining("\n"));
    }

    public static final class Entry {
        private final HTTPRequestPattern pattern;
        private final HTTPResponse response;
        private final Parsing parsing;

        private Entry(HTTPRequestPattern pattern, HTTPResponse response, Parsing parsing) {
            this.pattern = pattern;
            this.response = response;
            this.parsing = parsing;
        }

        public Parsing getParsing() {
            return parsing;
        }

        public HTTPResponse getResponse() {
            return response;
        }

        private HTTPRequestPattern getPattern() {
            return pattern;
        }

        @Override
        public String toString() {
            return String.format("Request pattern:%n%s%n" +
                            "Response:%n%s%n" +
                            "Request parsing: %s",
                    pattern, response, parsing);
        }
    }
}