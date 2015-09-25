package org.mb.http;

import com.google.common.base.Charsets;
import org.apache.log4j.Logger;
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
    private final static String LOG_REQUEST = "Request:\n%s";
    private final static String LOG_RESPONSE = "Response:\n%s";
    private static final String LOG_GLOBAL_PARSING = "Global parsing:\n%s";
    private static final Logger Log = Logger.getLogger(MainHandler.class);

    private final static String PARSING = "parsing";
    private final static String REQUEST = "request";

    private final Settings settings;

    public MainHandler(Settings settings) {
        this.settings = settings;
        Log.info(String.format(LOG_GLOBAL_PARSING, settings.getParsing()));
    }

    @Override
    public Response handle(final Request request) {
        Log.info(String.format(LOG_REQUEST, request));

        Parsing globalParsing = settings.getParsing();
        final Map<String, String> parsingResult = globalParsing.parse(request.getContent());

        ResponseDataMapping.ResponseData responseData = settings.getMapping().find(request, parsingResult);
        final Response response = responseData.getResponse();
        Log.info(String.format(LOG_RESPONSE, response));
        final InputStream inputStream = response.getContent().getStream();

        Parsing requestParsing = responseData.getParsing();
        parsingResult.putAll(requestParsing.parse(request.getContent()));

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
                throw new UnsupportedOperationException();
            }
        });
        return resultResponse;
    }
}