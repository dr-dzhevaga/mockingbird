package org.mb.http;

import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 17.06.2015.
 */
public class HTTPServerImpl implements HTTPServer {

    private Server server;

    public static HTTPServerFactoryImpl getFactory() {
        return new HTTPServerFactoryImpl();
    }

    public static class HTTPServerFactoryImpl implements HTTPServerFactory {
        @Override
        public HTTPServer create(int port) {
            return new HTTPServerImpl(port);
        }
    }

    HTTPServerImpl(int port) {
        server = new Server(port);
    }

    @Override
    public void start() throws Exception {
        if(server.getHandler() == null) {
            throw new IllegalStateException("Handler is not set");
        }
        server.start();
    }

    @Override
    public void join() throws InterruptedException {
        server.join();
    }

    @Override
    public void setHandler(Handler handler) {
        server.setHandler(new HandlerImpl(handler));
    }

    private static class HandlerImpl extends AbstractHandler {
        private Handler handler;

        public HandlerImpl(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
            HTTPRequest httpRequest = convertRequest(httpServletRequest);
            HTTPResponse httpResponse = handler.handle(httpRequest);
            convertResponse(httpResponse, httpServletResponse);
            request.setHandled(true);
        }

        private HTTPRequest convertRequest(HttpServletRequest from) throws IllegalArgumentException, IOException {
            String uri = from.getRequestURI();

            HTTPMethod method = HTTPMethod.fromString(from.getMethod());

            Enumeration<String> headerNames = from.getHeaderNames();

            Map<String, String> headers = Maps.newHashMap();
            while(headerNames.hasMoreElements()){
                String headerName = headerNames.nextElement();
                headers.put(headerName, from.getHeader(headerName));
            }

            ListMultimap<String, String> parameters = ArrayListMultimap.create();
            for(Map.Entry<String, String[]> parameter : from.getParameterMap().entrySet()) {
                parameters.putAll(parameter.getKey(), Arrays.asList(parameter.getValue()));
            }

            String encoding = Strings.isNullOrEmpty(from.getCharacterEncoding()) ? "UTF-8" : from.getCharacterEncoding();
            String content = CharStreams.toString(new InputStreamReader(from.getInputStream(), encoding));

            return new HTTPRequest(uri, method, parameters, headers, content);
        }

        private void convertResponse(HTTPResponse from, HttpServletResponse to) throws IOException {
            to.setStatus(from.getStatusCode());

            for(Map.Entry<String, String> header : from.getHeaders().entrySet()) {
                to.setHeader(header.getKey(), header.getValue());
            }

            try(InputStream inputStream = from.getContent()) {
                ByteStreams.copy(inputStream, to.getOutputStream());
            }
        }
    }
}
