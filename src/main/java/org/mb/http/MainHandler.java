package org.mb.http;

import com.google.common.base.Charsets;
import org.apache.log4j.Logger;
import org.mb.http.basic.Content;
import org.mb.http.basic.Request;
import org.mb.http.basic.Response;
import org.mb.http.basic.Handler;
import org.mb.http.mapping.Mapping;
import org.mb.jspl.JSPLikeProcessor;
import org.mb.settings.Settings;
import org.mb.parsing.Parsing;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.io.Reader;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 11.09.2015.
 */
public class MainHandler implements Handler {
    private static final String LOG_REQUEST = "Request:%n%s";
    private static final String LOG_RESPONSE = "Response:%n%s";
    private static final String LOG_GLOBAL_PARSING = "Global parsing:%n%s";
    private static final Logger LOG = Logger.getLogger(MainHandler.class);

    private static final String PARSING = "parsing";
    private static final String REQUEST = "request";

    private final Settings settings;

    public MainHandler(Settings settings) {
        this.settings = settings;
        LOG.info(String.format(LOG_GLOBAL_PARSING, settings.getParsing()));
    }

    @Override
    public Response handle(final Request request) {
        LOG.info(String.format(LOG_REQUEST, request));

        Parsing globalParsing = settings.getParsing();
        final Map<String, String> parsingResult = globalParsing.parse(request.getContent());

        Mapping.MappingEntry mappingEntry = settings.getMapping().resolve(request, parsingResult);
        final Response response = mappingEntry.getResponse();
        final InputStream inputStream = response.getContent().getStream();
        LOG.info(String.format(LOG_RESPONSE, response));

        Parsing requestParsing = mappingEntry.getParsing();
        parsingResult.putAll(requestParsing.parse(request.getContent()));

        Response resultResponse = response.setContent(new Content() {
            @Override
            public void writeTo(OutputStream outputStream) throws IOException {
                Writer writer = new OutputStreamWriter(outputStream, Charsets.UTF_8);
                try (
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