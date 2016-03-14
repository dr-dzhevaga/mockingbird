package org.mb.utils;

import java.util.Arrays;

/**
 * Created by Dmitriy Dzhevaga on 23.09.2015.
 */
public final class EnumUtils {
    private static final String NO_ENUM_CONSTANT = "No enum constant <%s>. One of %s is expected.";

    private EnumUtils() { }

    public static <T extends Enum<T>> T valueOf(Class<T> enumClass, String name) {
        for (T value : enumClass.getEnumConstants()) {
            if (value.name().compareToIgnoreCase(name) == 0) {
                return value;
            }
        }
        throw new IllegalArgumentException(String.format(NO_ENUM_CONSTANT, name, Arrays.toString(enumClass.getEnumConstants())));
    }
}
