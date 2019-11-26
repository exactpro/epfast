package com.exactpro.epfast.annotation.processing;

import org.junit.jupiter.api.Test;

import static com.exactpro.epfast.annotation.processing.FastFieldElement.recoverFastFieldNameFromSetter;
import static com.exactpro.epfast.annotation.processing.NamedFastPackage.isValidPackage;
import static com.exactpro.epfast.annotation.processing.helpers.FastPackageNameEncoder.encode;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FastProcessorTests {

    @Test
    void testFastPackageNameValidation() {
        assertTrue(isValidPackage(""));
        assertTrue(isValidPackage("com.exactpro"));
        assertTrue(isValidPackage("com.EXACTPRO"));
        assertTrue(isValidPackage("exacptro"));
        assertTrue(isValidPackage("com.exactpro.epfast.internal"));
        assertTrue(isValidPackage("$com.exactpro.epfast.internal"));
        assertTrue(isValidPackage("_com.exactpro12"));

        assertFalse(isValidPackage("com.exactpro."));
        assertFalse(isValidPackage("com.exactpro!"));
        assertFalse(isValidPackage("com.exactpro??"));
        assertFalse(isValidPackage(".com.EXACPTRO"));
        assertFalse(isValidPackage("1com.EXACTPRO"));
        assertFalse(isValidPackage("1Com.exactpro2"));
        assertFalse(isValidPackage("com.1exactpro"));
    }

    @Test
    void testPackageEncoding() {
        assertEquals("com$", encode("com"));
        assertEquals("com$.exactpro$", encode("com.exactpro"));
        assertEquals("$", encode(""));
    }

    @Test
    void testGetFieldNameFromSetter() {
        assertEquals("name", recoverFastFieldNameFromSetter("setName"));
        assertEquals("age", recoverFastFieldNameFromSetter("setAge"));
        assertEquals("a", recoverFastFieldNameFromSetter("setA"));
        assertEquals("company", recoverFastFieldNameFromSetter("setCompany"));
    }
}
