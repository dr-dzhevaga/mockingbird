package org.mb.loader.marshalling;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 28.06.2015.
 */
public class CommonMarshaller {
    private static final String GET_AS_TYPE_ERROR = "%s is expected";
    private static final String EMPTY_PARAMETER_ERROR = "Not empty %s is expected";
    private static final String GET_AS_STRING_ERROR = "Either string or number is expected";
    private static final String GET_AS_LIST_ERROR = "Either string or list of strings is expected";
    private static final String GET_AS_MAP_ERROR = "Map is expected";

    public static <T> T toType(Object o, Class<T> type, boolean notNull) throws MarshallingException {
        if(notNull && o == null) {
            throw new MarshallingException(String.format(EMPTY_PARAMETER_ERROR, type.getSimpleName()));
        }
        if(!type.isAssignableFrom(o.getClass())) {
            throw new MarshallingException(String.format(GET_AS_TYPE_ERROR, type.getSimpleName()));
        }
        return type.cast(o);
    }

    public static String toString(Object o) throws MarshallingException {
        if(o == null) {
            return "";
        }
        if(o instanceof String) {
            return (String)o;
        }
        else if(o instanceof Number) {
            return String.valueOf(((Number) o).intValue());
        }
        else {
            throw new MarshallingException(GET_AS_STRING_ERROR);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> toListOfType(Object o, Class<T> type) throws MarshallingException {
        if(o == null) {
            return Collections.emptyList();
        }
        if(type.isAssignableFrom(o.getClass())) {
            return Lists.<T>newArrayList((T) o);
        } else if(o instanceof List) {
            for(Object listItem : (List<Object>)o) {
                if(type.isAssignableFrom(o.getClass())) {
                    throw new MarshallingException(GET_AS_LIST_ERROR);
                }
            }
            return (List<T>)o;
        } else {
            throw new MarshallingException(GET_AS_LIST_ERROR);
        }
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> toMapOfType(Object o, Class<K> keyType, Class<V> valueType) throws MarshallingException {
        if(o == null) {
            return Collections.emptyMap();
        }
        if(o instanceof Map) {
            Map<Object, Object> map = (Map<Object, Object>)o;
            for(Map.Entry<Object, Object> entry : map.entrySet()) {
                toType(entry.getKey(), keyType, true);
                toType(entry.getValue(), valueType, true);
            }
            return (Map<K, V>) o;
        } else {
            throw new MarshallingException(GET_AS_MAP_ERROR);
        }
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Multimap<K, V> toMultimapOfType(Object o, Class<K> keyType, Class<V> valueType) throws MarshallingException {
        ListMultimap<K, V> multimap = ArrayListMultimap.create();
        if(o == null) {
            return multimap;
        }
        if(o instanceof Map) {
            Map<Object, Object> map = (Map<Object, Object>)o;
            for(Map.Entry<Object, Object> entry : map.entrySet()) {
                multimap.putAll(toType(entry.getKey(), keyType, true), toListOfType(entry.getValue(), valueType));
            }
            return multimap;
        } else {
            throw new MarshallingException(GET_AS_MAP_ERROR);
        }
    }
}