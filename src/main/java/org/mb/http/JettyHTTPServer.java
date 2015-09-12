package org.mb.http;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.mb.http.basic.*;

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

    private HTTPRequest readRequest(HttpServletRequest srcRequest) throws IllegalArgumentException, IOException {
        HTTPRequest.Builder builder = HTTPRequest.newBuilder(srcRequest.getRequestURI(), HTTPMethod.of(srcRequest.getMethod()));

        Enumeration<String> headerNames = srcRequest.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String headerName = headerNames.nextElement();
            String headerValue = srcRequest.getHeader(headerName);
            builder.addHeader(headerName, headerValue);
        }

        for(Map.Entry<String, String[]> parameter : srcRequest.getParameterMap().entrySet()) {
            String parameterName = parameter.getKey();
            List<String> parameterValues = Arrays.asList(parameter.getValue());
            builder.addQueryParameters(parameterName, parameterValues);
        }

        String encoding = srcRequest.getCharacterEncoding();
        if(Strings.isNullOrEmpty(encoding)){
            encoding = Charsets.UTF_8.toString();
        };

        String content = CharStreams.toString(new InputStreamReader(srcRequest.getInputStream(), encoding));
        builder.setContent(content);

        return builder.build();
    }

    private void writeResponse(HTTPResponse srcResponse, HttpServletResponse dstResponse) throws IOException {
        dstResponse.setStatus(srcResponse.getStatusCode());

        for(Map.Entry<String, String> header : srcResponse.getHeaders().entrySet()) {
            dstResponse.setHeader(header.getKey(), header.getValue());
        }

        try(InputStream inputStream = srcResponse.getContent()) {
            ByteStreams.copy(inputStream, dstResponse.getOutputStream());
        }
    }
}
