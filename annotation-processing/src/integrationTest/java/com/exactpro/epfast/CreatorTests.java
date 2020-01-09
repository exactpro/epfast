package com.exactpro.epfast;

import com.exactpro.epfast.inside.DefAnn;
import com.exactpro.epfast.inside.foo.FastTypeAnnotated;
import com.exactpro.epfast.withoutfasttype.FreshmanStudent;
import org.exactpro.epfast.RootFastType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreatorTests {
    private static ICreator creator;

    private static ICreator insideCreator;

    private static ICreator rootCreator;

    private static ICreator woFastTypeCreator;

    @BeforeAll
    static void setUp() {
        creator = CreatorService.getCreator("packageA");
        insideCreator = CreatorService.getCreator("com.exactpro.epfast.inside");
        rootCreator = CreatorService.getCreator("org.exactpro.epfast");
        woFastTypeCreator = CreatorService.getCreator("com.exactpro.epfast.withoutfasttype");
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
    void testCreatorNonDefaultAnnotation() {
        assertEquals(NamedAnnotated.class, creator.createSetter("named").getObject().getClass());
    }

    @Test
    void testRootCreator() {
        assertEquals(RootFastType.class, rootCreator.createSetter("root").getObject().getClass());
    }

    @Test
    void testInsideCreator() {
        assertEquals(DefAnn.class, insideCreator.createSetter("DefAnn").getObject().getClass());
    }

    @Test
    void testSameFastTypesAreProcessedInDifferentPackages() {
        assertEquals(com.exactpro.epfast.inside.NamedAnnotated.class,
            insideCreator.createSetter("named").getObject().getClass());
        assertEquals(NamedAnnotated.class,
            creator.createSetter("named").getObject().getClass());
    }

    @Test
    void testFastTypeWithoutPackage() {
        assertEquals(FastTypeAnnotated.class, insideCreator.createSetter("FastTypeAnnotated").getObject().getClass());
    }

    @Test
    void testExceptionThrown() {
        Exception exception = assertThrows(RuntimeException.class, () -> creator.createSetter("null"));
        assertEquals("FastType name=\"null\" not found", exception.getMessage());
    }

    @Test
    void testFieldSettings() {
        IFieldSetter fieldSetter = creator.createSetter("Student");
        fieldSetter.setField("name", "John");
        fieldSetter.setField("age", 22);

        Student createdObject = (Student) fieldSetter.getObject();
        assertEquals("John", createdObject.getName());
        assertEquals(22, createdObject.getAge());
    }

    @Test
    void testOverridenFieldIsNotPresentFromParent() {
        IFieldSetter fieldSetter = creator.createSetter("ThirdGradeStudent");
        fieldSetter.setField("name", "John");
        fieldSetter.setField("lastName", "Doe");
        fieldSetter.setField("years", 15);
        ThirdGradeStudent createdObject = (ThirdGradeStudent) fieldSetter.getObject();
        assertEquals("John", createdObject.getName());
        assertEquals(21, createdObject.getAge());
        assertEquals("Doe", createdObject.getLastName());
    }

    @Test
    void testInheritedFieldsFromBaseWithoutFastType() {
        IFieldSetter fieldSetter = woFastTypeCreator.createSetter("FreshmanStudent");
        fieldSetter.setField("firstName", "John");
        fieldSetter.setField("lastName", "Doe");
        FreshmanStudent createdObject = (FreshmanStudent) fieldSetter.getObject();
        assertEquals("John", createdObject.getName());
        assertEquals(18, createdObject.getAge());
        assertEquals("Doe", createdObject.getLastName());
    }

    @Test
    void testDefaultFields() {
        IFieldSetter fieldSetter = creator.createSetter("DefaultFieldStudent");
        fieldSetter.setField("name", "John");
        fieldSetter.setField("age", 22);
        DefaultFieldStudent createdObject = (DefaultFieldStudent) fieldSetter.getObject();
        assertEquals("John", createdObject.getName());
        assertEquals(22, createdObject.getAge());
    }

    @Test
    void testFieldOverride() {
        IFieldSetter setter = creator.createSetter("BClass");
        setter.setField("XXX", 25);
        setter.setField("YYY", 50L);
        setter.setField("name", "name");
        BClass bClass = (BClass) setter.getObject();
        assertEquals(50, bClass.getFoo());
        assertEquals("name", bClass.getName());
        assertEquals(25, bClass.getSuperFoo());
    }
}
