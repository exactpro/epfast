package com.exactpro.epfast.annotation.processing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TypeNameTest {
    @Test
    void testRootClasses() {
        TypeName typeName = new TypeName("SomeClass");
        assertEquals("SomeClass", typeName.getClassName());
        assertEquals("", typeName.getPackageName());
        assertEquals("SomeClass", typeName.toString());
    }

    @Test
    void testMultiplePackagedClass() {
        TypeName typeName = new TypeName("path.to.some.random.Class");
        assertEquals("path.to.some.random", typeName.getPackageName());
        assertEquals("Class", typeName.getClassName());
        assertEquals("path.to.some.random.Class", typeName.toString());
    }

    @Test
    void testSinglePackagedClass() {
        TypeName typeName = new TypeName("package.Class");
        assertEquals("package", typeName.getPackageName());
        assertEquals("Class", typeName.getClassName());
        assertEquals("package.Class", typeName.toString());
    }

}
