
import org.mb.binding.HTTPBinding;
import org.mb.http.*;
import org.mb.marshalling.Marshaller;
import org.mb.parsing.InputFormat;
import org.mb.parsing.Parser;
import org.mb.parsing.ParserFactory;

import java.io.FileReader;

/**
 * Created by Dmitriy Dzhevaga on 17.06.2015.
 */
public class Test {
    public static void main(String[] args) throws Exception {
        Parser parser = ParserFactory.newParser(InputFormat.JSON);
        Object objectsGraph = parser.parse(new FileReader("settings.json"));
        final HTTPBinding responseResolver = Marshaller.GetAsHTTPBinding(objectsGraph);
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
