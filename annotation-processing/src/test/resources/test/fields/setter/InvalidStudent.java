package test.fields.setter;

import com.exactpro.epfast.annotations.FastField;
import com.exactpro.epfast.annotations.FastType;

@FastType
public class InvalidStudent {

    private String name;

    @SuppressWarnings({"checkstyle:methodName"})
    @FastField(name = "name")
    public void SetName(String name) {
        this.name = name;
    }

}
