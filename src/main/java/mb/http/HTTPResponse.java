package mb.http;

import com.google.common.collect.Maps;
import java.util.Map;

/**
 * Created by Дмитрий on 17.06.2015.
 */
public class HTTPResponse {
    private final int statusCode;
    private final Map<String, String> headers;
    private final String content;

    public HTTPResponse(int statusCode, Map<String, String> headers, String content) {
        this.statusCode = statusCode;
        this.headers = Maps.newHashMap(headers);
        this.content = content;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return getStatusCode() + " response";
    }
}
