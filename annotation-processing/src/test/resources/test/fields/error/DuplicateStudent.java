package test.fields.error;

import com.exactpro.epfast.annotations.FastField;
import com.exactpro.epfast.annotations.FastType;

import java.util.List;
@FastType
public class DuplicateStudent {

    private String name;

    private String lastName;

    private int age;

    public String getName() {
        return name;
    }

    @FastField(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    @FastField(name = "name")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getAge() {
        return age;
    }

    @FastField(name = "age")
    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" +
            "name='" + name + '\'' +
            ", lastName='" + lastName + '\'' +
            ", age=" + age +
            '}';
    }
}
