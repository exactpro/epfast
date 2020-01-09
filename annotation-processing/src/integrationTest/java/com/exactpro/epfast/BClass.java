package com.exactpro.epfast;

import com.exactpro.epfast.annotations.FastField;
import com.exactpro.epfast.annotations.FastType;

@FastType
public class BClass extends AClass {

    private long foo;

    @FastField(name = "YYY")
    public void setFoo(long foo) {
        this.foo = foo;
    }

    public long getFoo() {
        return foo;
    }

    @Override
    public String toString() {
        return "foo=" + foo +
            ", name='" + getName() + '\'' +
            ", superFoo=" + getSuperFoo();
    }
}
