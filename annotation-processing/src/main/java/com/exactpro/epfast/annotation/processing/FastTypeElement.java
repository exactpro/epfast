package com.exactpro.epfast.annotation.processing;

import com.exactpro.epfast.annotation.processing.helpers.FastPackageNameEncoder;
import com.exactpro.epfast.annotation.processing.helpers.FieldSetterNameGenerator;
import com.exactpro.epfast.annotations.FastType;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.List;

public class FastTypeElement {
    private String fastTypeName;

    private Element fastTypeElement;

    private ArrayList<FastFieldElement> fastFields = new ArrayList<>();

    private FastPackageElement fastPackage;

    public FastTypeElement(Element fastTypeElement) {
        this.fastTypeName = getNameFromElement(fastTypeElement);
        this.fastTypeElement = fastTypeElement;
    }

    private String getNameFromElement(Element fastTypeElement) {
        String fastType = fastTypeElement.getAnnotation(FastType.class).name();
        if (fastType.isEmpty()) {
            return fastTypeElement.getSimpleName().toString();
        } else {
            return fastType;
        }
    }

    public void addFastField(FastFieldElement fastField) {
        fastFields.add(fastField);
    }

    public String getTypeName() {
        return fastTypeName;
    }

    public Element getElement() {
        return fastTypeElement;
    }

    public List<FastFieldElement> getFastFields() {
        return fastFields;
    }

    public TypeName getFieldSetterTypeName() {
        return new TypeName(
            String.format("com.exactpro.epfast.annotation.internal.%s.%s",
                FastPackageNameEncoder.encode(fastPackage.getPackageName()),
                FieldSetterNameGenerator.generateClassName(fastTypeName))
        );
    }

    public void setPackage(FastPackageElement fastPackage) {
        this.fastPackage = fastPackage;
    }

}
