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
    void testCreatorDefaultAnnotation() throws Exception {
        assertEquals(DefaultAnnotated.class, creator.create("DefaultAnnotated").getClass());
    }

    @Test
    void testCreatorNonDefaultAnnotation() throws Exception {
        assertEquals(NamedAnnotated.class, creator.create("named").getClass());
    }

    @Test
    void testRootCreator() throws Exception {
        assertEquals(RootFastType.class, rootCreator.create("root").getClass());
    }

    @Test
    void testInsideCreator() throws Exception {
        assertEquals(DefAnn.class, insideCreator.create("DefAnn").getClass());
    }

    @Test
    void testSameFastTypesAreProcessedInDifferentPackages() throws Exception {
        assertEquals(com.exactpro.epfast.inside.NamedAnnotated.class, insideCreator.create("named").getClass());
        assertEquals(com.exactpro.epfast.NamedAnnotated.class, creator.create("named").getClass());
    }

    /*
        com.exactpro.epfast.inside.foo package isn't @FastPackage annotated. so all classes inside it
        (FastTypeAnnotated) must be distributed to outer FastPackage ("com.exacptro.epfast.inside").
    */
    @Test
    void testFastTypeWithoutPackage() throws Exception {
        assertEquals(FastTypeAnnotated.class, insideCreator.create("FastTypeAnnotated").getClass());
    }

    @Test
    void testExceptionThrown() {
        Exception exception = assertThrows(RuntimeException.class, () -> creator.create("null"));
        assertEquals("FastType name=\"null\" not found", exception.getMessage());
    }
}
