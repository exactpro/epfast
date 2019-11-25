package com.exactpro.epfast;

import com.exactpro.epfast.inside.DefAnn;
import com.exactpro.epfast.inside.foo.FastTypeAnnotated;
import org.exactpro.epfast.RootFastType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreatorTests {
    private static ICreator creator;

    private static ICreator insideCreator;

    private static ICreator rootCreator;

    @BeforeAll
    static void setUp() {
        creator = CreatorService.getCreator("packageA");
        insideCreator = CreatorService.getCreator("com.exactpro.epfast.inside");
        rootCreator = CreatorService.getCreator("org.exactpro.epfast");
    }

    @Test
    void testGetProviderByFastPackage() {
        assertEquals(creator.getFastPackage(), "packageA");
        assertEquals(insideCreator.getFastPackage(), "com.exactpro.epfast.inside");
        assertEquals(rootCreator.getFastPackage(), "org.exactpro.epfast");
        Exception exception = assertThrows(RuntimeException.class, () ->
            CreatorService.getCreator("com.exactpro.epfast"));
        assertEquals("Creator with FastPackage=\"com.exactpro.epfast\" not found", exception.getMessage());
    }

    @Test
    void testCreatorNonDefaultAnnotation() throws Exception {
        assertEquals(NamedAnnotated.class, creator.create("named").getObject().getClass());
    }

    @Test
    void testRootCreator() throws Exception {
        assertEquals(RootFastType.class, rootCreator.create("root").getObject().getClass());
    }

    @Test
    void testInsideCreator() throws Exception {
        assertEquals(DefAnn.class, insideCreator.create("DefAnn").getObject().getClass());
    }

    @Test
    void testSameFastTypesAreProcessedInDifferentPackages() throws Exception {
        assertEquals(com.exactpro.epfast.inside.NamedAnnotated.class,
            insideCreator.create("named").getObject().getClass());
        assertEquals(com.exactpro.epfast.NamedAnnotated.class,
            creator.create("named").getObject().getClass());
    }

    @Test
    void testFastTypeWithoutPackage() throws Exception {
        assertEquals(FastTypeAnnotated.class, insideCreator.create("FastTypeAnnotated").getObject().getClass());
    }

    @Test
    void testExceptionThrown() {
        Exception exception = assertThrows(RuntimeException.class, () -> creator.create("null"));
        assertEquals("FastType name=\"null\" not found", exception.getMessage());
    }

    @Test
    void testFieldSettings() throws Exception {
        IFieldSetter<Student> fieldSetter = creator.create("Student");
        fieldSetter.setField("name", "John");
        fieldSetter.setField("age", 22);

        Student createdObject = fieldSetter.getObject();
        assertEquals("John", createdObject.getName());
        assertEquals(22, createdObject.getAge());
    }

    @Test
    void testOverridenFieldIsNotPresentFromParent() throws Exception {
        IFieldSetter<ThirdGradeStudent> fieldSetter = creator.create("ThirdGradeStudent");
        fieldSetter.setField("name", "John");
        fieldSetter.setField("lastName", "Doe");
        fieldSetter.setField("years", 15);
        ThirdGradeStudent createdObject = fieldSetter.getObject();
        assertEquals("John", createdObject.getName());
        assertEquals(21, createdObject.getAge());
        assertEquals("Doe", createdObject.getLastName());
    }
}
