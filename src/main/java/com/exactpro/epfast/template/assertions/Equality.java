package com.exactpro.epfast.template.assertions;

public class Equality {

    public static boolean areNotEqual(String item1, String item2) {
        if (item1 == null || item2 == null) {
            return !(item1 == null && item2 == null);
        }
        return !item1.equals(item2);
    }
}
