package org.mb.http.mapping.utils;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Dmitriy Dzhevaga on 05.07.2015.
 */
public final class RegexPatternMap {

    private final Map<String, RegexPattern> patternMap;

    private RegexPatternMap() {
        this.patternMap = Maps.newHashMap();
    }

    public static RegexPatternMap newInstance() {
        return new RegexPatternMap();
    }

    public void add(final String name, final String valueRegex) {
        patternMap.put(name, RegexPattern.from(valueRegex));
    }

    public void addAll(final Map<String, String> parameters) {
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            add(entry.getKey(), entry.getValue());
        }
    }

    public boolean matches(final Map<String, String> checkedMap) {
        for (Map.Entry<String, RegexPattern> entry : this.patternMap.entrySet()) {
            RegexPattern regexPattern = entry.getValue();
            String value = checkedMap.get(entry.getKey());
            if (!regexPattern.matches(value)) {
                return false;
            }
        }
        return true;
    }

    public boolean matches(final Multimap<String, String> checkedMap) {
        for (Map.Entry<String, RegexPattern> entry : this.patternMap.entrySet()) {
            RegexPattern regexPattern = entry.getValue();
            if (!checkedMap.containsKey(entry.getKey())) {
                return false;
            }
            Collection<String> values = checkedMap.get(entry.getKey());
            for (String value : values) {
                if (!regexPattern.matches(value)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isEmpty() {
        return patternMap.isEmpty();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RegexPatternMap)) {
            return false;
        }
        final RegexPatternMap other = (RegexPatternMap) obj;
        return Objects.equals(this.patternMap, other.patternMap);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.patternMap);
    }

    @Override
    public String toString() {
        return patternMap.toString();
    }
}