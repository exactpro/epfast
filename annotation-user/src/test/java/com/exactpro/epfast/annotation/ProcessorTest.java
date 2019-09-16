package com.exactpro.epfast.annotation;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProcessorTest {
    @Test
    public void testUnique() throws Exception {
        Object unique = new CreatorImpl().create("unique");
        assertEquals(Unique.class, unique.getClass());
    }
    @Test
    public void testDefault() throws Exception{
        Object def = new CreatorImpl().create("Default");
        assertEquals(Default.class, def.getClass());
    }
    @Test(expected = RuntimeException.class)
    public void testNull() throws Exception{
        new CreatorImpl().create("null");
    }

}
