package com.exactpro.epfast.annotation;

import org.junit.Test;

import com.exactpro.epfast.annotation.internal.CreatorImpl;

import static org.junit.Assert.assertEquals;

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
    @Test(expected = RuntimeException.class)
    public void testNull() {
        new CreatorImpl().create("null");
    }

}
