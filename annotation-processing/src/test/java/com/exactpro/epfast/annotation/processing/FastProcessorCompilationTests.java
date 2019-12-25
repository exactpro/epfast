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
            "",
            "META-INF/services/com.exactpro.epfast.ICreator")
            .withStringContents(StandardCharsets.UTF_8,
                "com.exactpro.epfast.annotation.internal.test$.CreatorImpl\n");
    }

    @Test
    void testJavaPackageEqualsFastPackageFails() {
        JavaSourcesSubject.assertThat(
            JavaFileObjects.forResource("test/DefaultAnnotated.java"),
            JavaFileObjects.forResource("test/packagea/FastTypeElement.java"),
            JavaFileObjects.forResource("test/packagea/package-info.java"))
            .processedWith(fastProcessor)
            .failsToCompile()
            .withErrorContaining("Both anonymous and named package(s) refer to FAST package test.");
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
            JavaFileObjects.forResource("test/insidea/FastTypeElement.java"),
            JavaFileObjects.forResource("test/insideb/FastTypeElement.java"),
            JavaFileObjects.forResource("test/packagea/FastTypeElement.java"),
            JavaFileObjects.forResource("test/packagea/package-info.java"))
            .processedWith(fastProcessor)
            .failsToCompile()
            .withErrorContaining("Both anonymous and named package(s) refer to FAST package test.");
    }

    @Test
    void testSameFastTypesInDifferentPackagesSucceeds() {
        JavaSourcesSubject.assertThat(
            JavaFileObjects.forResource("test/insidea/FastTypeElement.java"),
            JavaFileObjects.forResource("test/insideb/FastTypeElement.java"))
            .processedWith(fastProcessor)
            .compilesWithoutError()
            .and().generatesFileNamed(StandardLocation.CLASS_OUTPUT,
            "com.exactpro.epfast.annotation.internal.test$",
            "CreatorImpl.class")
            .and().generatesFileNamed(StandardLocation.CLASS_OUTPUT,
            "",
            "META-INF/services/com.exactpro.epfast.ICreator");
    }

    @Test
    void testDuplicateFastPackagesFail() {
        JavaSourcesSubject.assertThat(
            JavaFileObjects.forResource("test/packagea/FastTypeElement.java"),
            JavaFileObjects.forResource("test/packageb/FastTypeElement.java"),
            JavaFileObjects.forResource("test/packagea/package-info.java"),
            JavaFileObjects.forResource("test/packageb/package-info.java"))
            .processedWith(fastProcessor)
            .failsToCompile()
            .withErrorContaining("Multiple @FastPackage annotations referring FAST package test are found.");
    }

    @Test
    void testAbstractFastTypeFails() {
        JavaSourcesSubject.assertThat(
            JavaFileObjects.forResource("test/constructor/AbstractClass.java"))
            .processedWith(fastProcessor)
            .failsToCompile()
            .withErrorContaining(
                "Class annotated with @FastType should be instantiable with default constructor");
    }

    @Test
    void testInnerFastTypeFails() {
        JavaSourcesSubject.assertThat(
            JavaFileObjects.forResource("test/constructor/InnerClass.java"))
            .processedWith(fastProcessor)
            .failsToCompile()
            .withErrorContaining(
                "Class annotated with @FastType should be instantiable with default constructor");
    }

    @Test
    void testPrivateConstructorFails() {
        JavaSourcesSubject.assertThat(
            JavaFileObjects.forResource("test/constructor/PrivateConstructorClass.java"))
            .processedWith(fastProcessor)
            .failsToCompile()
            .withErrorContaining(
                "Class annotated with @FastType should be instantiable with default constructor");
    }

    @Test
    void testPackagePrivateClassFails() {
        JavaSourcesSubject.assertThat(
            JavaFileObjects.forResource("test/constructor/PrivateClass.java"))
            .processedWith(fastProcessor)
            .failsToCompile()
            .withErrorContaining(
                "Class annotated with @FastType should be instantiable with default constructor");
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
    void testInheritedFastFieldDuplicates() {
        JavaSourcesSubject.assertThat(
            JavaFileObjects.forResource("test/inherit/Student.java"),
            JavaFileObjects.forResource("test/inherit/ThirdGradeStudent.java"))
            .processedWith(fastProcessor)
            .failsToCompile()
            .withErrorContaining("Multiple @FastField annotations referring FAST field 'name' are found.");
    }

    @Test
    void testPrivateFastFieldFails() {
        JavaSourcesSubject.assertThat(
            JavaFileObjects.forResource("test/fields/priv/Student.java"))
            .processedWith(fastProcessor)
            .failsToCompile()
            .withErrorContaining("@FastField must be a valid java bean setter");
    }

    @Test
    void testMultiParameterFastFieldFails() {
        JavaSourcesSubject.assertThat(
            JavaFileObjects.forResource("test/fields/multiparameter/Student.java"))
            .processedWith(fastProcessor)
            .failsToCompile()
            .withErrorContaining("@FastField must be a valid java bean setter");
    }

    @Test
    void testFastFieldNameEqualsSetFails() {
        JavaSourcesSubject.assertThat(
            JavaFileObjects.forResource("test/fields/setter/Student.java"))
            .processedWith(fastProcessor)
            .failsToCompile()
            .withErrorContaining("@FastField must be a valid java bean setter");
    }

    @Test
    void testInvalidJavaSetter() {
        JavaSourcesSubject.assertThat(
            JavaFileObjects.forResource("test/fields/setter/InvalidStudent.java"))
            .processedWith(fastProcessor)
            .failsToCompile()
            .withErrorContaining("@FastField must be a valid java bean setter");
    }

    @Test
    void testInvalidOverride() {
        JavaSourcesSubject.assertThat(
            JavaFileObjects.forResource("test/fields/override/invalid/Student.java"),
            JavaFileObjects.forResource("test/fields/override/invalid/FreshmanStudent.java"))
            .processedWith(fastProcessor).failsToCompile()
            .withErrorContaining("Multiple @FastField annotations referring FAST field 'name' are found");

    }

    @Test
    void testValidOverride() {
        JavaSourcesSubject.assertThat(
            JavaFileObjects.forResource("test/fields/override/valid/Student.java"),
            JavaFileObjects.forResource("test/fields/override/valid/FreshmanStudent.java"))
            .processedWith(fastProcessor)
            .compilesWithoutError()
            .and().generatesFileNamed(StandardLocation.CLASS_OUTPUT,
            "com.exactpro.epfast.annotation.internal.test$.fields$.override$.valid$",
            "CreatorImpl.class");
    }

    @Test
    void testMultiLevelInheritance() {
        JavaSourcesSubject.assertThat(
            JavaFileObjects.forResource("test/fields/multilevelinheritance/A.java"),
            JavaFileObjects.forResource("test/fields/multilevelinheritance/B.java"),
            JavaFileObjects.forResource("test/fields/multilevelinheritance/C.java"),
            JavaFileObjects.forResource("test/fields/multilevelinheritance/D.java"))
            .processedWith(fastProcessor)
            .compilesWithoutError();
    }
}
