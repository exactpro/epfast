package test;

import com.exactpro.epfast.annotations.FastField;

public class NonFastType {
    private String name;

    public String getName() {
        return name;
    }
    @FastField(name = "name")
    public void setName(String name) {
        this.name = name;
    }
}
