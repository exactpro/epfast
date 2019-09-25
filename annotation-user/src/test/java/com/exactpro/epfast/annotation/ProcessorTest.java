package com.exactpro.epfast.annotation;

import com.exactpro.epfast.annotation.internal.CreatorImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProcessorTest {
    @Test
    void testUnique() {
        Object unique = new CreatorImpl().create("unique");
        assertEquals(Unique.class, unique.getClass());
    }

    @Test
    void testDefault() {
        Object def = new CreatorImpl().create("Default");
        assertEquals(Default.class, def.getClass());
    }

    @Test
    void testNull() {
        Assertions.assertThrows(RuntimeException.class, () -> new CreatorImpl().create("null"));
    }

}
