package test.fields;

import com.exactpro.epfast.annotations.FastField;
import com.exactpro.epfast.annotations.FastType;

import java.util.List;

@FastType
public class Student {

    private String name;

    private int age;

    public String getName() {
        return name;
    }

    @FastField(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    @FastField(name = "age")
    public void setAge(Integer age) {
        this.age = age;
    }

}