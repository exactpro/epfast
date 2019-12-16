package test.fields.setter;

import com.exactpro.epfast.annotations.FastField;
import com.exactpro.epfast.annotations.FastType;

import java.util.List;

@FastType
public class InvalidStudent {

    private String name;

    @FastField(name = "name")
    public void SetName(String name) {
        this.name = name;
    }

}
