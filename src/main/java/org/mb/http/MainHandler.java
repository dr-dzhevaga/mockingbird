package org.mb.http;

import com.google.common.base.Charsets;
import org.mb.http.basic.Content;
import org.mb.http.basic.Request;
import org.mb.http.basic.Response;
import org.mb.http.basic.Handler;
import org.mb.http.mapping.ResponseDataMapping;
import org.mb.jspl.JSPLikeProcessor;
import org.mb.settings.Settings;
import org.mb.parsing.Parsing;

import java.io.*;
import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 11.09.2015.
 */
public class MainHandler implements Handler {
    private final static String PARSING = "parsing";
    private final static String REQUEST = "request";
    private final Settings settings;

    public MainHandler(Settings settings) {
        this.settings = settings;
    }

    @Override
    public Response handle(final Request request) {
        Parsing commonParsing = settings.getParsing();
        final Map<String, String> parsingResult = commonParsing.parse(request.getContent());

        ResponseDataMapping.ResponseData responseData = settings.getMapping().find(request, parsingResult);
        final Response response = responseData.getResponse();
        Parsing requestParsing = responseData.getParsing();

        parsingResult.putAll(requestParsing.parse(request.getContent()));

        final InputStream inputStream = response.getContent().getStream();
        Response resultResponse = response.setContent(new Content() {
            @Override
            public void writeTo(OutputStream outputStream) throws IOException {
                Writer writer = new OutputStreamWriter(outputStream, Charsets.UTF_8);
                try(
                    Reader reader = new InputStreamReader(inputStream, Charsets.UTF_8);
                ) {
                    JSPLikeProcessor.from(reader).
                            put(PARSING, parsingResult).
                            put(REQUEST, request.toMap()).
                            print(writer);
                }
            }

            @Override
            public InputStream getStream() {
                return response.getContent().getStream();
            }
        });
        return resultResponse;
    }
}