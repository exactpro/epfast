package test.inherit;

import com.exactpro.epfast.annotations.FastField;
import com.exactpro.epfast.annotations.FastType;

@FastType
public class ThirdGradeStudent extends Student {
    private Integer age = 21;

    private String lastName;

    @Override
    public Integer getAge() {
        return age;
    }

    public String getLastName() {
        return lastName;
    }

    @FastField(name = "name")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
