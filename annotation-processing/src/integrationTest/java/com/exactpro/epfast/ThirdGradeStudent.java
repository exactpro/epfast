package com.exactpro.epfast;

import com.exactpro.epfast.annotations.FastField;

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

    @Override
    public void setAge(Integer age) {
        this.age = age;
    }

    @FastField(name = "lastName")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
