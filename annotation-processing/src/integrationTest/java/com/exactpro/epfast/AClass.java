package com.exactpro.epfast;

import com.exactpro.epfast.annotations.FastField;
import com.exactpro.epfast.annotations.FastType;

@FastType
public class AClass {

    private int superFoo;

    private String name;

    @FastField(name = "XXX")
    public void setFoo(int foo) {
        this.superFoo = foo;
    }

    @FastField(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    public int getSuperFoo() {
        return superFoo;
    }

    public String getName() {
        return name;
    }
}
