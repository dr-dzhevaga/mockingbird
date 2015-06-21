import mb.http.*;

import java.util.Collections;


/**
 * Created by Dmitriy Dzhevaga on 17.06.2015.
 */
public class Test {
    public static void main(String[] args) throws Exception {
        final ResponseResolver responseResolver = new ResponseResolver();

        HTTPRequestPattern requestPattern = HTTPRequestPattern.getBuilder().addMethod(HTTPMethod.GET).build();
        HTTPResponse response = new HTTPResponse(200, Collections.<String, String>emptyMap(), "Hello GET");
        responseResolver.addMapping(requestPattern, response);

        requestPattern = HTTPRequestPattern.getBuilder().addMethod(HTTPMethod.POST).addHeader("Content-Type", "text/html").addHeader("Content-Type", "text/plain").build();
        response = new HTTPResponse(200, Collections.<String, String>emptyMap(), "Hello POST with text");
        responseResolver.addMapping(requestPattern, response);

        requestPattern = HTTPRequestPattern.getBuilder().addMethod(HTTPMethod.POST).addHeader("Content-Type", "application/json").build();
        response = new HTTPResponse(200, Collections.<String, String>emptyMap(), "Hello POST with json");
        responseResolver.addMapping(requestPattern, response);

        requestPattern = HTTPRequestPattern.getBuilder().addMethod(HTTPMethod.POST).build();
        response = new HTTPResponse(200, Collections.<String, String>emptyMap(), "Hello POST");
        responseResolver.addMapping(requestPattern, response);

        HTTPServer server = HTTPServerImpl.getFactory().create(8080);

        server.setHandler(new Handler() {
            @Override
            public HTTPResponse handle(HTTPRequest request) {
                return responseResolver.resolve(request);
            }
        });
        server.start();
        server.join();
    }
}
