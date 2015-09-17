package org.mb.http;

import org.mb.http.basic.Content;
import org.mb.http.basic.Request;
import org.mb.http.basic.Response;
import org.mb.http.basic.Handler;
import org.mb.http.mapping.ResponseDataMapping;
import org.mb.scripting.Engine;
import org.mb.scripting.JSEngine;
import org.mb.scripting.JSPLikePreprocessor;
import org.mb.scripting.JSPLikeProcessor;
import org.mb.settings.Settings;
import org.mb.parsing.Parsing;

import java.io.*;
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
        Parsing commonParsing = settings.getParsing();
        final Map<String, String> parsingResult = commonParsing.parse(request.getContent());

        ResponseDataMapping.ResponseData responseData = settings.getMapping().find(request, parsingResult);

        Parsing requestParsing = responseData.getParsing();
        parsingResult.putAll(requestParsing.parse(request.getContent()));

        final Response response = responseData.getResponse();
        final InputStream inputStream = response.getContent().toStream();
        return response.setContent(new Content() {
            @Override
            public void writeTo(OutputStream outputStream) throws IOException {
                JSPLikeProcessor.from(inputStream).to(outputStream);
            }

            @Override
            public InputStream toStream() {
                return response.getContent().toStream();
            }
        });
    }
}