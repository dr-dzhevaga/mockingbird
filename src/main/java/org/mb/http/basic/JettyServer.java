package org.mb.http.basic;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.io.CharStreams;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * Created by Dmitriy Dzhevaga on 17.06.2015.
 */
public class JettyServer implements Server {
    private static final String LOG_NO_RESPONSE_ERROR = "No response will be sent";
    private static final Logger Log = Logger.getLogger(JettyServer.class);

    private org.eclipse.jetty.server.Server jettyServer;

    public static Server newInstance(int port) {
        return new JettyServer(port);
    }

    private JettyServer(int port) {
        jettyServer = new org.eclipse.jetty.server.Server(port);
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
            public void handle(String s, org.eclipse.jetty.server.Request httpRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
                try {
                    Request request = readRequest(httpServletRequest);
                    Response response = handler.handle(request);
                    writeResponse(response, httpServletResponse);
                    httpRequest.setHandled(true);
                } catch (Exception e) {
                    if(httpServletResponse.isCommitted()) {
                        Log.error(LOG_NO_RESPONSE_ERROR);
                    }
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private Request readRequest(HttpServletRequest srcRequest) throws IllegalArgumentException, IOException {
        Request.Builder builder = Request.newBuilder(srcRequest.getRequestURI(), Method.of(srcRequest.getMethod()));

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
        }

        String content = CharStreams.toString(new InputStreamReader(srcRequest.getInputStream(), encoding));
        builder.setContent(content);

        return builder.build();
    }

    private void writeResponse(Response srcResponse, HttpServletResponse dstResponse) throws IOException {
        dstResponse.setStatus(srcResponse.getStatusCode());

        for(Map.Entry<String, String> header : srcResponse.getHeaders().entrySet()) {
            dstResponse.setHeader(header.getKey(), header.getValue());
        }

        srcResponse.getContent().writeTo(dstResponse.getOutputStream());
    }
}
