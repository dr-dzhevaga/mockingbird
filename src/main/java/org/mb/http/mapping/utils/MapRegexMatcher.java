package org.mb.http.mapping.utils;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import java.util.Map;
import java.util.Objects;

/**
 * Created by Dmitriy Dzhevaga on 05.07.2015.
 */
public final class MapRegexMatcher {

    private final Map<String, RegexMatcher> patternMap;

    private MapRegexMatcher() {
        this.patternMap = Maps.newHashMap();
    }

    public static MapRegexMatcher newInstance() {
        return new MapRegexMatcher();
    }

    public void add(String name, String valueRegex) {
        patternMap.put(name, RegexMatcher.from(valueRegex));
    }

    public void addAll(Map<String, String> parameters) {
        parameters.forEach(this::add);
    }

    public boolean matches(Map<String, String> map) {
        return patternMap.entrySet().stream().allMatch(pattern ->
                pattern.getValue().matches(map.get(pattern.getKey())));
    }

    public boolean matches(Multimap<String, String> map) {
        return patternMap.entrySet().stream().allMatch(pattern ->
                map.containsKey(pattern.getKey()) &&
                        map.get(pattern.getKey()).stream().allMatch(pattern.getValue()::matches));
    }

    public boolean isEmpty() {
        return patternMap.isEmpty();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MapRegexMatcher)) {
            return false;
        }
        MapRegexMatcher other = (MapRegexMatcher) obj;
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