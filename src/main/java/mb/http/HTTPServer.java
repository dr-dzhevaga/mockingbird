package mb.http;

/**
 * Created by Dmitriy Dzhevaga on 17.06.2015.
 */
public interface HTTPServer {
    public void start() throws Exception;
    public void join() throws InterruptedException;
    public void setHandler(Handler handler);
}
