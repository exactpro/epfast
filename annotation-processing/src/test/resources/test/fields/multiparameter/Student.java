package test.fields.multiparameter;

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
    public void setName(String name, String lastName) {
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
