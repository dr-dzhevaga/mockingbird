package org.mb.http.basic;

/**
 * Created by Dmitriy Dzhevaga on 17.06.2015.
 */
public interface HTTPServer {
    public void start() throws Exception;
    public void setHandler(Handler handler);
}