package test.fields.override.valid;

import com.exactpro.epfast.annotations.FastField;
import com.exactpro.epfast.annotations.FastType;

@FastType
public class FreshmanStudent extends Student {

    private String name;

    @FastField(name = "name")
    public void setName(String name) {
        this.name = name;
    }

}
