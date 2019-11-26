package com.exactpro.epfast.annotation.processing.helpers;

import java.util.Base64;

public class FieldSetterNameGenerator {
    private static Base64.Encoder encoder = Base64.getEncoder().withoutPadding();

    public static String generateClassName(String fastTypeName) {
        String hexString = "A" + encoder.encodeToString(fastTypeName.getBytes());
        return hexString;
    }
}
