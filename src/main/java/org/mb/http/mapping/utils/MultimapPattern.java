package org.mb.http.mapping.utils;

import com.google.common.collect.Multimap;
import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 05.07.2015.
 */
public class MultimapPattern<T1, T2> {

    private final Multimap<T1, T2> pattern;

    private MultimapPattern(Multimap<T1, T2> pattern) {
        this.pattern = pattern;
    }

    public static <T1, T2> MultimapPattern<T1, T2> from(Multimap<T1, T2> pattern) {
        return new MultimapPattern<>(pattern);
    }

    public boolean matches(Map<T1, T2> checked) {
        for (T1 key : pattern.keySet()) {
            if(!pattern.get(key).contains(checked.get(key)))
                return false;
        }
        return true;
    }

    public boolean matches(Multimap<T1, T2> checked) {
        for (T1 key : pattern.keySet()) {
            if(checked.get(key).isEmpty() || !pattern.get(key).containsAll(checked.get(key)))
                return false;
        }
        return true;
    }
}