package org.mb.http;

import org.apache.log4j.Logger;
import org.mb.http.basic.HTTPRequest;
import org.mb.http.basic.HTTPResponse;
import org.mb.http.basic.Handler;
import org.mb.http.mapping.Mapping;
import org.mb.jspl.JSPLikeProcessor;
import org.mb.settings.Settings;

import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 11.09.2015.
 */
public final class MainHandler implements Handler {
    private static final Logger log = Logger.getLogger(MainHandler.class);
    private final Settings settings;

    public MainHandler(Settings settings) {
        this.settings = settings;
        log.info(String.format("Global parsing:%n%s", settings.getParsing()));
    }

    @Override
    public HTTPResponse handle(HTTPRequest request) throws Exception {
        log.info(String.format("Request:%n%s", request));
        // parse request content with global parsing
        Map<String, String> parsedContent = settings.getParsing().parse(request.getContent());
        // resolve response settings in mapping
        Mapping.Entry responseSettings = settings.getMapping().resolve(request, parsedContent);
        // get response
        HTTPResponse response = responseSettings.getResponse();
        log.info(String.format("Response:%n%s", response));
        // parse request content with request specific parsing
        parsedContent.putAll(responseSettings.getParsing().parse(request.getContent()));
        // process jsp-like macros in content writer
        return response.setContentWriter((input, output) -> {
            new JSPLikeProcessor(input, output).
                    putInContext("parsing", parsedContent).
                    putInContext("request", request.toMap()).
                    print();
        });
    }
}