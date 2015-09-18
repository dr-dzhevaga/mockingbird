package org.mb.http;

import com.google.common.base.Charsets;
import org.mb.http.basic.Content;
import org.mb.http.basic.Request;
import org.mb.http.basic.Response;
import org.mb.http.basic.Handler;
import org.mb.http.mapping.ResponseDataMapping;
import org.mb.scripting.JSPLikeProcessor;
import org.mb.settings.Settings;
import org.mb.parsing.Parsing;
import sun.org.mozilla.javascript.internal.Scriptable;

import java.io.*;
import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 11.09.2015.
 */
public class MainHandler implements Handler {
    private final String PARSING = "parsing";
    private final String REQUEST = "request";
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

        final InputStream inputStream = response.getContent().toStream();
        return response.setContent(new Content() {
            @Override
            public void writeTo(OutputStream outputStream) throws IOException {
                try(
                    Reader reader = new InputStreamReader(inputStream, Charsets.UTF_8);
                    Writer writer = new OutputStreamWriter(outputStream, Charsets.UTF_8);
                ) {
                    JSPLikeProcessor.from(reader).
                            put(PARSING, parsingResult).
                            put(REQUEST, request).
                            compile(writer);
                }
            }

            @Override
            public InputStream toStream() {
                return response.getContent().toStream();
            }
        });
    }
}