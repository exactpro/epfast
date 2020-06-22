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
