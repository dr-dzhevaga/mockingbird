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
    private final static String LOG_REQUEST = "Request is received:\n%s";
    private final static String LOG_PARSING_RESULT_FIRST = "Parsing result after processing with global parsing:\n%s";
    private final static String LOG_RESPONSE = "Response:\n%s";
    private final static String LOG_PARSING_RESULT_SECOND = "Parsing result after processing with request parsing:\n%s";
    private static final Logger Log = Logger.getLogger(MainHandler.class);

    private final static String PARSING = "parsing";
    private final static String REQUEST = "request";

    private final Settings settings;

    public MainHandler(Settings settings) {
        this.settings = settings;
    }

    @Override
    public Response handle(final Request request) {
        Log.info(String.format(LOG_REQUEST, request));

        Parsing globalParsing = settings.getParsing();
        final Map<String, String> parsingResult = globalParsing.parse(request.getContent());
        Log.debug(String.format(LOG_PARSING_RESULT_FIRST, parsingResult));

        ResponseDataMapping.ResponseData responseData = settings.getMapping().find(request, parsingResult);
        final Response response = responseData.getResponse();
        final InputStream inputStream = response.getContent().getStream();
        Log.info(String.format(LOG_RESPONSE, response));

        Parsing requestParsing = responseData.getParsing();
        parsingResult.putAll(requestParsing.parse(request.getContent()));
        Log.debug(String.format(LOG_PARSING_RESULT_SECOND, parsingResult));

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