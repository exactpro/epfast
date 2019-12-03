package com.exactpro.epfast.withoutfasttype;

import com.exactpro.epfast.annotations.FastField;
import com.exactpro.epfast.annotations.FastType;

@FastType
public class FreshmanStudent extends StudentWOFastType {
    private Integer age = 18;

    private String lastName;

    @Override
    public Integer getAge() {
        return age;
    }

    public String getLastName() {
        return lastName;
    }

    @FastField(name = "lastName")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
