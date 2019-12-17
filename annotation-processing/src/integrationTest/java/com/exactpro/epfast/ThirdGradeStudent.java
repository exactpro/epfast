package com.exactpro.epfast;

import com.exactpro.epfast.annotations.FastField;
import com.exactpro.epfast.annotations.FastType;

@FastType
public class ThirdGradeStudent extends Student {

    private String lastName;

    @Override
    public Integer getAge() {
        return 21;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    @FastField(name = "years")
    public void setAge(Integer age) {

    }

    @FastField(name = "lastName")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
