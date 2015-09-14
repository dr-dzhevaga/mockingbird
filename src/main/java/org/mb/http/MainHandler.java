package org.mb.http;

import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import org.mb.http.basic.Request;
import org.mb.http.basic.Response;
import org.mb.http.basic.Handler;
import org.mb.http.mapping.HandlerDataMapping;
import org.mb.parsing.Parser;
import org.mb.parsing.ParserFactory;
import org.mb.parsing.InputFormat;
import org.mb.parsing.ParsingException;
import org.mb.settings.Settings;

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
        Map<String, String> parsingResults = Maps.newHashMap();

        Table<InputFormat, String, String> parsing = settings.getParsing();
        for(InputFormat inputFormat : parsing.rowKeySet()) {
            Parser parser = ParserFactory.newParser(inputFormat, request.getContent());
            try {
                parsingResults.putAll(parser.parse(parsing.row(inputFormat)));
            } catch (ParsingException e) {
                throw new RuntimeException(e);
            }
        }

        HandlerDataMapping.HandlerData handlerData = settings.getMapping().find(request, parsingResults);
        return handlerData.getResponse();
    }
}