package org.mb.binding;

import com.google.common.collect.Multimap;
import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 05.07.2015.
 */
public class Utils {
    public static <T1, T2> boolean checkMap(Map<T1, T2> checked, Multimap<T1, T2> rules) {
        for (T1 key : rules.keySet()) {
            if(!rules.get(key).contains(checked.get(key)))
                return false;
        }
        return true;
    }

    public static <T1, T2> boolean checkMultimap(Multimap<T1, T2> checked, Multimap<T1, T2> rules) {
        for (T1 key : rules.keySet()) {
            if(rules.get(key).size() != checked.get(key).size())
                return false;
            if(!(rules.get(key).containsAll(checked.get(key)) && checked.get(key).containsAll(rules.get(key))))
                return false;
        }
        return true;
    }
}
