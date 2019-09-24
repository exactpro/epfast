package com.exactpro.epfast.annotation;



import com.exactpro.epfast.annotation.internal.CreatorImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProcessorTest {
    @Test
    public void testUnique() {
        Object unique = new CreatorImpl().create("unique");
        assertEquals(Unique.class, unique.getClass());
    }
    @Test
    public void testDefault() {
        Object def = new CreatorImpl().create("Default");
        assertEquals(Default.class, def.getClass());
    }
    @Test
    public void testNull() {
        Assertions.assertThrows(RuntimeException.class, () -> new CreatorImpl().create("null"));
    }

}
