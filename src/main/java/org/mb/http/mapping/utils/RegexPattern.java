package org.mb.http.mapping.utils;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Created by Dmitriy Dzhevaga on 28.09.2015.
 */
public class RegexPattern {
    private final Pattern pattern;

    private RegexPattern(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    public static RegexPattern from(String regex) {
        return new RegexPattern(regex);
    }

    public boolean matches(String string) {
        return string != null && pattern.matcher(string).matches();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RegexPattern)) {
            return false;
        }
        final RegexPattern other = (RegexPattern)obj;
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
