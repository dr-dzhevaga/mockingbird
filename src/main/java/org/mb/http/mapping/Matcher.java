package org.mb.http.mapping;

import com.google.common.collect.Multimap;
import java.util.Map;

/**
 * Created by Dmitriy Dzhevaga on 05.07.2015.
 */
public class Matcher<T1, T2> {

    private final Multimap<T1, T2> rules;

    private Matcher(Multimap<T1, T2> rules) {
        this.rules = rules;
    }

    public static <T1, T2> Matcher<T1, T2> withRules(Multimap<T1, T2> rules) {
        return new Matcher<>(rules);
    }

    public boolean matches(Map<T1, T2> checked) {
        for (T1 key : rules.keySet()) {
            if(!rules.get(key).contains(checked.get(key)))
                return false;
        }
        return true;
    }

    public boolean matches(Multimap<T1, T2> checked) {
        for (T1 key : rules.keySet()) {
            if(rules.get(key).size() != checked.get(key).size())
                return false;
            if(!(rules.get(key).containsAll(checked.get(key)) && checked.get(key).containsAll(rules.get(key))))
                return false;
        }
        return true;
    }
}