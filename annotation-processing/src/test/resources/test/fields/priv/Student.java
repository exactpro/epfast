package test.fields.priv;

import com.exactpro.epfast.annotations.FastField;
import com.exactpro.epfast.annotations.FastType;

@FastType
public class Student {

    private String name;

    private int age;

    public String getName() {
        return name;
    }

    @FastField(name = "name")
    private void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    @FastField(name = "age")
    private void setAge(Integer age) {
        this.age = age;
    }

}
