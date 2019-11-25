package test.inherit;

import com.exactpro.epfast.annotations.FastField;
import com.exactpro.epfast.annotations.FastType;

import java.util.List;

@FastType
public class Student {

    private String name;

    private List<Integer> grades;

    private Integer age;

    public String getName() {
        return name;
    }

    @FastField(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getGrades() {
        return grades;
    }

    public void setGrades(List<Integer> grades) {
        this.grades = grades;
    }

    public Integer getAge() {
        return age;
    }

    @FastField(name = "age")
    public void setAge(Integer age) {
        this.age = age;
    }
}
