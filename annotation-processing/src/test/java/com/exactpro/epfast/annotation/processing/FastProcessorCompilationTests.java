package com.exactpro.epfast.annotation.processing;

import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourcesSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.tools.StandardLocation;
import java.nio.charset.StandardCharsets;

class FastProcessorCompilationTests {
    private FastProcessor fastProcessor;

    @BeforeEach
    void setUp() {
        fastProcessor = new FastProcessor();
    }

    @Test
    void testProcessing() {
        JavaSourcesSubject.assertThat(JavaFileObjects.forResource("test/DefaultAnnotated.java"))
            .processedWith(fastProcessor)
            .compilesWithoutError()
            .and().generatesFileNamed(StandardLocation.CLASS_OUTPUT,
            "com.exactpro.epfast.annotation.internal.test$",
            "CreatorImpl.class")
            .and().generatesFileNamed(StandardLocation.CLASS_OUTPUT,
            "",
            "META-INF/services/com.exactpro.epfast.ICreator")
            .withStringContents(StandardCharsets.UTF_8,
                "com.exactpro.epfast.annotation.internal.test$.CreatorImpl\n");
    }

    @Test
    void testNotAnnotatedProcessing() {
        JavaSourcesSubject.assertThat(JavaFileObjects.forResource("test/NoProcessing.java"))
            .processedWith(fastProcessor)
            .compilesWithoutError();
    }

    @Test
    void testMultipleSourceProcessing() {
        JavaSourcesSubject.assertThat(
            JavaFileObjects.forResource("test/NoProcessing.java"),
            JavaFileObjects.forResource("test/DefaultAnnotated.java"))
            .processedWith(fastProcessor)
            .compilesWithoutError()
            .and().generatesFileNamed(StandardLocation.CLASS_OUTPUT,
                "com.exactpro.epfast.annotation.internal.test$",
                "CreatorImpl.class")
            .and().generatesFileNamed(StandardLocation.CLASS_OUTPUT,
            "com.exactpro.epfast.annotation.internal.test$",
            "DefaultAnnotatedFieldSetter.class")
            .and().generatesFileNamed(StandardLocation.CLASS_OUTPUT,
            "",
            "META-INF/services/com.exactpro.epfast.ICreator")
            .withStringContents(StandardCharsets.UTF_8,
                "com.exactpro.epfast.annotation.internal.test$.CreatorImpl\n");
    }

    @Test
    void testJavaPackageEqualsFastPackageFails() {
        JavaSourcesSubject.assertThat(
            JavaFileObjects.forResource("test/DefaultAnnotated.java"),
            JavaFileObjects.forResource("test/packageA/FastTypeElement.java"),
            JavaFileObjects.forResource("test/packageA/package-info.java"))
            .processedWith(fastProcessor)
            .failsToCompile()
            .withErrorContaining("Multiple @FastPackage annotations referring FAST package test are found.");
    }

    @Test
    void testDuplicateFastTypeNamesAreNotProcessed() {
        //when duplicates processed - FAIL
        JavaSourcesSubject.assertThat(
            JavaFileObjects.forResource("test/DuplicateA.java"),
            JavaFileObjects.forResource("test/DuplicateB.java"))
            .processedWith(fastProcessor)
            .failsToCompile()
            .withErrorContaining(
                "Multiple @FastType annotations referring FAST type 'duplicate' are found.");
    }

    @Test
    void testRootPackageEqualsFastPackageFails() {
        JavaSourcesSubject.assertThat(
            JavaFileObjects.forResource("test/insideA/FastTypeElement.java"),
            JavaFileObjects.forResource("test/insideB/FastTypeElement.java"),
            JavaFileObjects.forResource("test/packageA/FastTypeElement.java"),
            JavaFileObjects.forResource("test/packageA/package-info.java"))
            .processedWith(fastProcessor)
            .failsToCompile()
            .withErrorContaining(
                "Multiple @FastPackage annotations referring FAST package test are found.");
    }

    @Test
    void testSameFastTypesInDifferentPackagesSucceeds() {
        JavaSourcesSubject.assertThat(
            JavaFileObjects.forResource("test/insideA/FastTypeElement.java"),
            JavaFileObjects.forResource("test/insideB/FastTypeElement.java"))
            .processedWith(fastProcessor)
            .compilesWithoutError()
            .and().generatesFileNamed(StandardLocation.CLASS_OUTPUT,
            "com.exactpro.epfast.annotation.internal.test$",
            "CreatorImpl.class")
            .and().generatesFileNamed(StandardLocation.CLASS_OUTPUT,
                "",
                "META-INF/services/com.exactpro.epfast.ICreator")
            .and().generatesFileNamed(StandardLocation.CLASS_OUTPUT,
            "com.exactpro.epfast.annotation.internal.test$",
            "fastAFieldSetter.class")
            .and().generatesFileNamed(StandardLocation.CLASS_OUTPUT,
            "com.exactpro.epfast.annotation.internal.test$",
            "fastBFieldSetter.class");
    }

    @Test
    void testDuplicateFieldsFail() {
        JavaSourcesSubject.assertThat(
            JavaFileObjects.forResource("test/fields/error/DuplicateStudent.java"),
            JavaFileObjects.forResource("test/fields/error/package-info.java"))
            .processedWith(fastProcessor)
            .failsToCompile()
            .withErrorContaining(
                "Multiple @FastField annotations referring FAST field 'name' are found.");
    }

    @Test
    void testNonDefaultConstructorFastTypeFails() {
        JavaSourcesSubject.assertThat(
            JavaFileObjects.forResource("test/NonEmptyConstructorFastType.java"))
            .processedWith(fastProcessor)
            .failsToCompile()
            .withErrorContaining("@FastTypes must be instantiatable with no argument constructor");
    }

    @Test
    void testInheritedFastFieldDuplicates() {
        JavaSourcesSubject.assertThat(
            JavaFileObjects.forResource("test/inherit/Student.java"),
            JavaFileObjects.forResource("test/inherit/ThirdGradeStudent.java"))
            .processedWith(fastProcessor)
            .failsToCompile()
            .withErrorContaining("Multiple @FastField annotations referring FAST field 'name' are found.");
    }

    @Test
    void testInheritedFastFieldOverrides() {
        JavaSourcesSubject.assertThat(
            JavaFileObjects.forResource("test/inherit/Student.java"),
            JavaFileObjects.forResource("test/inherit/ThirdGradeStudent.java"))
            .processedWith(fastProcessor)
            .failsToCompile()
            .withErrorContaining("Multiple @FastField annotations referring FAST field 'name' are found.");
    }
}
