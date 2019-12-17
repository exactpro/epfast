package test.fields.multilevelinheritance;

import com.exactpro.epfast.annotations.FastField;

public class B extends C {
    @FastField
    public void setFoo(int value) { }
}
