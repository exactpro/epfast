package com.exactpro.epfast.annotation.processing.helpers;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FastPackageNameEncoder {
    public static String encode(String rawPath) {
        return Arrays.stream(rawPath.split(Pattern.quote(".")))
            .map(x -> x + "$").collect(Collectors.joining("."));
    }
}
