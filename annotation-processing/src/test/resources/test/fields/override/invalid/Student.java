package test.fields.override.invalid;

import com.exactpro.epfast.annotations.FastField;
import com.exactpro.epfast.annotations.FastType;

@FastType
public class Student {

    private String name;

    @FastField(name = "name")
    public void setName(String name) {
        this.name = name;
    }

}
