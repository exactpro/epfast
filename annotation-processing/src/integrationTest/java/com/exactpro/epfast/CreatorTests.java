package com.exactpro.epfast;

import com.exactpro.epfast.annotation.internal.packages.com.exactpro.epfast.inside.CreatorImpl;
import com.exactpro.epfast.inside.DefAnn;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CreatorTests {
    private com.exactpro.epfast.annotation.internal.packages.com.exactpro.CreatorImpl creator =
        new com.exactpro.epfast.annotation.internal.packages.com.exactpro.CreatorImpl();

    private CreatorImpl insideCreator = new CreatorImpl();

    @Test
    void testCreatorDefaultAnnotation() {
        assertEquals(DefaultAnnotated.class, creator.create("DefaultAnnotated").getClass());
    }

    @Test
    void testCreatorNonDefaultAnnotation() {
        assertEquals(NamedAnnotated.class, creator.create("named").getClass());
    }

    @Test
    void testInsideCreator() {
        assertEquals(DefAnn.class, insideCreator.create("DefAnn").getClass());
    }

    @Test
    void testExceptionThrown() {
        Exception exception = assertThrows(RuntimeException.class, ()-> new CreatorImpl().create("null"));
        assertEquals("FastType name=\"null\" not found", exception.getMessage());
    }
}
