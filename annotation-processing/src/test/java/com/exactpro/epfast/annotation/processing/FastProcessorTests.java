/*
 * Copyright 2019-2020 Exactpro (Exactpro Systems Limited)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.exactpro.epfast.annotation.processing;

import org.junit.jupiter.api.Test;

import static com.exactpro.epfast.annotation.processing.FastFieldElement.getBeanPropertyName;
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
        assertEquals("name", getBeanPropertyName("setName"));
        assertEquals("age", getBeanPropertyName("setAge"));
        assertEquals("a", getBeanPropertyName("setA"));
        assertEquals("company", getBeanPropertyName("setCompany"));
    }
}
