package com.exactpro.epfast.annotation.processing;

import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourcesSubject;
import org.junit.jupiter.api.Test;

import javax.tools.StandardLocation;
import java.nio.charset.StandardCharsets;

class FastProcessorTest {

    @Test
    void testProcessing() {
        JavaSourcesSubject.assertThat(JavaFileObjects.forResource("test/DefaultAnnotated.java"))
            .processedWith(new FastProcessor())
            .compilesWithoutError()
            .and().generatesFileNamed(StandardLocation.CLASS_OUTPUT,
            "com.exactpro.epfast.annotation.internal",
            "CreatorImpl.class")
            .and().generatesFileNamed(StandardLocation.CLASS_OUTPUT,
            "",
            "META-INF/services/com.exactpro.epfast.ICreator")
            .withStringContents(StandardCharsets.UTF_8, "com.exactpro.epfast.annotation.internal.CreatorImpl\n");
    }

    @Test
    void testNotAnnotatedProcessing() {
        JavaSourcesSubject.assertThat(JavaFileObjects.forResource("test/NoProcessing.java"))
            .processedWith(new FastProcessor())
            .compilesWithoutError();
    }

    @Test
    void testMultipleSourceProcessing() {
        JavaSourcesSubject.assertThat(
            JavaFileObjects.forResource("test/NoProcessing.java"),
            JavaFileObjects.forResource("test/DefaultAnnotated.java"))
            .processedWith(new FastProcessor())
            .compilesWithoutError()
            .and().generatesFileNamed(StandardLocation.CLASS_OUTPUT,
                "com.exactpro.epfast.annotation.internal",
                "CreatorImpl.class")
            .and().generatesFileNamed(StandardLocation.CLASS_OUTPUT,
            "",
            "META-INF/services/com.exactpro.epfast.ICreator")
            .withStringContents(StandardCharsets.UTF_8, "com.exactpro.epfast.annotation.internal.CreatorImpl\n");
    }

    @Test
    void testDuplicateFastTypeNamesAreNotProcessed() {
        //when duplicates processed - FAIL
        JavaSourcesSubject.assertThat(
            JavaFileObjects.forResource("test/DuplicateA.java"),
            JavaFileObjects.forResource("test/DuplicateB.java"))
            .processedWith(new FastProcessor())
            .failsToCompile().withErrorContaining("Multiple declarations referring to FastType 'duplicate' are found.");
    }
}
