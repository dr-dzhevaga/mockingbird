package org.mb.scripting;

import com.google.common.collect.Maps;
import org.mb.http.basic.Request;
import sun.org.mozilla.javascript.internal.NativeObject;
import sun.org.mozilla.javascript.internal.Scriptable;

import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 18.09.2015.
 */
public class JavaToJSConverter {
    public static Scriptable convertMap(Map<String, ?> map) {
        NativeObject nativeObject = new NativeObject() {
            @Override
            public Object getDefaultValue(Class<?> var1)  {
                return toString();
            }
        };
        for (Map.Entry<String,?> entry : map.entrySet()) {
            nativeObject.defineProperty(entry.getKey(), entry.getValue(), NativeObject.READONLY);
        }
        return nativeObject;
    }

    public static Scriptable convertRequest(Request request) {
        Map requestMap = Maps.newHashMap();
        requestMap.put("uri", request.getURI());
        requestMap.put("method", request.getMethod().toString());
        requestMap.put("headers", convertMap(request.getHeaders()));
        {
            Map queryMap = Maps.newHashMap();
            for(String key : request.getQueryParameters().keySet()) {
                queryMap.put(key, request.getQueryParameters().get(key).toArray());
            }
            requestMap.put("queryParameters", convertMap(queryMap));
        }
        return convertMap(requestMap);
    }
}