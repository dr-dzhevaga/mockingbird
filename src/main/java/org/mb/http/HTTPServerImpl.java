package org.mb.http;


import com.google.common.base.Optional;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
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
            HTTPRequest httpRequest = readRequest(httpServletRequest);
            HTTPResponse httpResponse = handler.handle(httpRequest);
            writeResponse(httpResponse, httpServletResponse);
            request.setHandled(true);
        }

        private HTTPRequest readRequest(HttpServletRequest source) throws IllegalArgumentException, IOException {
            String uri = source.getRequestURI();

            HTTPMethod method = HTTPMethod.fromString(source.getMethod());

            Enumeration<String> headerNames = source.getHeaderNames();

            Map<String, String> headers = new HashMap<String, String>();
            while(headerNames.hasMoreElements()){
                String headerName = headerNames.nextElement();
                headers.put(headerName, source.getHeader(headerName));
            }

            ListMultimap<String, String> parameters = ArrayListMultimap.create();
            for(Map.Entry<String, String[]> parameter : source.getParameterMap().entrySet()) {
                parameters.putAll(parameter.getKey(), Arrays.asList(parameter.getValue()));
            }

            String encoding = Optional.fromNullable(source.getCharacterEncoding()).or("UTF-8");
            String content = CharStreams.toString(new InputStreamReader(source.getInputStream(), encoding));

            return new HTTPRequest(uri, method, parameters, headers, content);
        }

        private void writeResponse(HTTPResponse source, HttpServletResponse destination) throws IOException {
            destination.setStatus(source.getStatusCode());

            for(Map.Entry<String, String> header : source.getHeaders().entrySet()) {
                destination.setHeader(header.getKey(), header.getValue());
            }

            destination.getOutputStream().print(source.getContent());
        }
    }
}
