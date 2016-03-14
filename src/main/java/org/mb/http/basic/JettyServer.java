package org.mb.http.basic;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.io.CharStreams;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;

/**
 * Created by Dmitriy Dzhevaga on 17.06.2015.
 */
public final class JettyServer implements HTTPServer {
    private static final Logger log = Logger.getLogger(JettyServer.class);
    private final Server server;

    public JettyServer(int port) {
        server = new Server(port);
    }

    @Override
    public void start() throws Exception {
        if (server.getHandler() == null) {
            throw new IllegalStateException("Handler is not set");
        }
        server.start();
        server.join();
    }

    @Override
    public void setHandler(final Handler handler) {
        server.setHandler(new AbstractHandler() {
            @Override
            public void handle(String target,
                               Request baseRequest,
                               HttpServletRequest servletRequest,
                               HttpServletResponse servletResponse) {
                try {
                    HTTPRequest request = readRequest(servletRequest);
                    HTTPResponse response = handler.handle(request);
                    writeResponse(response, servletResponse);
                    baseRequest.setHandled(true);
                } catch (Exception e) {
                    if (servletResponse.isCommitted()) {
                        log.error("No response will be sent");
                    }
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private HTTPRequest readRequest(HttpServletRequest src) throws IOException {
        // URI and method
        HTTPRequest.Builder builder = HTTPRequest.builder(src.getRequestURI(), HTTPMethod.of(src.getMethod()));
        // headers
        Enumeration<String> headerNames = src.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = src.getHeader(headerName);
            builder.addHeader(headerName, headerValue);
        }
        // query parameters
        src.getParameterMap().forEach(builder::addQueryParameters);
        // content
        String encoding = src.getCharacterEncoding();
        if (Strings.isNullOrEmpty(encoding)) {
            encoding = Charsets.UTF_8.toString();
        }
        String content = CharStreams.toString(new InputStreamReader(src.getInputStream(), encoding));
        builder.setContent(content);
        return builder.build();
    }

    private void writeResponse(HTTPResponse src, HttpServletResponse dst) throws Exception {
        // status
        dst.setStatus(src.getStatusCode());
        // headers
        src.getHeaders().forEach(dst::setHeader);
        // write content
        src.writeContentTo(dst.getOutputStream());
    }
}
