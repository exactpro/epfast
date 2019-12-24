package com.exactpro.epfast.decoder.dictionary;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TestDictionary {

    private FastDictionary dictionary = new FastDictionary();

    @Test
    void firstTest() {
        dictionary.addEntry("null", null);
        dictionary.addEntry("int", 7);
        dictionary.addEntry("string", "string");
        dictionary.addEntry("decimal", 7985.3);

        assertEquals(dictionary.getEntry("null"), Optional.empty());
        assertEquals(dictionary.getEntry("int"), Optional.of(7));
        assertEquals(dictionary.getEntry("string"), Optional.of("string"));
        assertEquals(dictionary.getEntry("decimal"), Optional.of(7985.3));
    }
}
