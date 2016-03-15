package org.mb.http;

import org.apache.log4j.Logger;
import org.mb.http.basic.HTTPRequest;
import org.mb.http.basic.HTTPResponse;
import org.mb.http.basic.Handler;
import org.mb.http.mapping.Mapping;
import org.mb.jspl.JSPLikeProcessor;
import org.mb.settings.Settings;

import java.io.File;
import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 11.09.2015.
 */
public final class MockingbirdHandler implements Handler {
    private static final Logger log = Logger.getLogger(MockingbirdHandler.class);
    private final File file;
    private final String format;
    private Settings settings;

    public MockingbirdHandler(File file, String format) throws Exception {
        this.file = file;
        this.format = format;
        this.settings = Settings.load(file, format);
        log.info(String.format("Global parsing:%n%s", settings.getParsing()));
    }

    @Override
    public HTTPResponse handle(HTTPRequest request) throws Exception {
        log.info(String.format("Request:%n%s", request));
        // make a reference copy to avoid an unpredictable settings change in the reload method
        Settings settingsCopy = settings;
        // parse request content with global parsing
        Map<String, String> parsedContent = settingsCopy.getParsing().parse(request.getContent());
        // resolve response settings in mapping
        Mapping.Entry responseSettings = settingsCopy.getMapping().resolve(request, parsedContent);
        HTTPResponse response = responseSettings.getResponse();
        log.info(String.format("Response:%n%s", response));
        // parse request content with request specific parsing
        parsedContent.putAll(responseSettings.getParsing().parse(request.getContent()));
        // process jsp-like macros in content writer
        return response.setContentWriter((input, output) ->
                new JSPLikeProcessor(input, output).
                        putInContext("parsing", parsedContent).
                        putInContext("request", request.toMap()).
                        print());
    }

    public void reloadSettings() {
        try {
            settings = Settings.load(file, format);
            log.debug("Settings are reloaded");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}