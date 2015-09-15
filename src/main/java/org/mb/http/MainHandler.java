package org.mb.http;

import com.google.common.collect.Maps;
import org.mb.http.basic.Request;
import org.mb.http.basic.Response;
import org.mb.http.basic.Handler;
import org.mb.http.mapping.HandlerDataMapping;
import org.mb.settings.Settings;
import org.mb.parsing.Parsing;

import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 11.09.2015.
 */
public class MainHandler implements Handler {
    private final Settings settings;

    public MainHandler(Settings settings) {
        this.settings = settings;
    }

    @Override
    public Response handle(Request request) {
        Map<String, String> parsingResult = Maps.newHashMap();
        Parsing parsing = settings.getParsing();
        parsingResult.putAll(parsing.parse(request.getContent()));
        HandlerDataMapping.HandlerData handlerData = settings.getMapping().find(request, parsingResult);
        return handlerData.getResponse();
    }
}