package org.mb.http;

import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * Created by Dmitriy Dzhevaga on 17.06.2015.
 */
public class JettyHTTPServer implements HTTPServer {

    private Server jettyServer;

    public static HTTPServer newInstance(int port) {
        return new JettyHTTPServer(port);
    }

    private JettyHTTPServer(int port) {
        jettyServer = new Server(port);
    }

    @Override
    public void start() throws Exception {
        if(jettyServer.getHandler() == null) {
            throw new IllegalStateException("Handler is not set");
        }
        jettyServer.start();
    }

    @Override
    public void join() throws InterruptedException {
        jettyServer.join();
    }

    @Override
    public void setHandler(final Handler handler) {
        jettyServer.setHandler(new AbstractHandler() {
            @Override
            public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
                HTTPRequest httpRequest = readRequest(httpServletRequest);
                HTTPResponse httpResponse = handler.handle(httpRequest);
                writeResponse(httpResponse, httpServletResponse);
                request.setHandled(true);
            }
        });
    }

    private HTTPRequest readRequest(HttpServletRequest from) throws IllegalArgumentException, IOException {
        HTTPRequest.Builder builder = HTTPRequest.getBuilder(from.getRequestURI(), HTTPMethod.of(from.getMethod()));

        Enumeration<String> headerNames = from.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String headerName = headerNames.nextElement();
            String headerValue = from.getHeader(headerName);
            builder.addHeader(headerName, headerValue);
        }

        for(Map.Entry<String, String[]> parameter : from.getParameterMap().entrySet()) {
            String parameterName = parameter.getKey();
            List<String> parameterValues = Arrays.asList(parameter.getValue());
            builder.addQueryParameters(parameterName, parameterValues);
        }

        String encoding = Strings.isNullOrEmpty(from.getCharacterEncoding()) ? "UTF-8" : from.getCharacterEncoding();
        String content = CharStreams.toString(new InputStreamReader(from.getInputStream(), encoding));
        builder.setContent(content);

        return builder.build();
    }

    private void writeResponse(HTTPResponse from, HttpServletResponse to) throws IOException {
        to.setStatus(from.getStatusCode());

        for(Map.Entry<String, String> header : from.getHeaders().entrySet()) {
            to.setHeader(header.getKey(), header.getValue());
        }

        try(InputStream inputStream = from.getContent()) {
            ByteStreams.copy(inputStream, to.getOutputStream());
        }
    }
}
