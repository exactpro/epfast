package com.exactpro.epfast.annotation.processing;

import org.junit.jupiter.api.Test;

import static com.exactpro.epfast.annotation.processing.AnonymousFastPackage.getDeepestCommonPackagePath;
import static org.junit.jupiter.api.Assertions.*;

class AnonymousFastPackageTest {
    @Test
    void testDeepestCommonPackagePath() {
        String aPath = "com.exactpro.epfast";
        String bPath = "com.exactpro$.epfast";
        assertEquals("com", getDeepestCommonPackagePath(aPath, bPath));

        aPath = "Com.exactpro";
        bPath = "com.exactpro";
        assertEquals("", getDeepestCommonPackagePath(aPath, bPath));

        aPath = "";
        bPath = "exactpro";
        assertEquals("", getDeepestCommonPackagePath(aPath, bPath));

        aPath = "com.exactpro";
        bPath = "org.exactpro";
        assertEquals("", getDeepestCommonPackagePath(aPath, bPath));

    }
}
