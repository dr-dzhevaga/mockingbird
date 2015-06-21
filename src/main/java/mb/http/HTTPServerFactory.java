package mb.http;

/**
 * Created by Dmitriy Dzhevaga on 17.06.2015.
 */
public interface HTTPServerFactory {
    public HTTPServer create(int port);
}
