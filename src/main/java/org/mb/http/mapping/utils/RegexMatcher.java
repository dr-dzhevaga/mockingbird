package org.mb.http.mapping.utils;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Created by Dmitriy Dzhevaga on 28.09.2015.
 */
public final class RegexMatcher {
    private final Pattern pattern;

    private RegexMatcher(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    public static RegexMatcher from(String regex) {
        return new RegexMatcher(regex);
    }

    public boolean matches(String string) {
        return string != null && pattern.matcher(string).matches();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RegexMatcher)) {
            return false;
        }
        RegexMatcher other = (RegexMatcher) obj;
        return Objects.equals(this.pattern.pattern(), other.pattern.pattern());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.pattern.pattern());
    }

    @Override
    public String toString() {
        return pattern.pattern();
    }
}
