package com.exactpro.epfast.annotation.processing;

import com.exactpro.epfast.annotations.FastField;
import com.exactpro.epfast.annotations.FastType;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.List;

public class FastTypeElement {
    private String fastTypeName;

    private Element fastTypeElement;

    private List<FastFieldElement> fastFields;

    public FastTypeElement(Element fastTypeElement) {
        this.fastTypeName = getNameFromElement(fastTypeElement);
        this.fastTypeElement = fastTypeElement;
        this.fastFields = processFastFields(fastTypeElement);
    }

    private List<FastFieldElement> processFastFields(Element fastTypeElement) {
        ArrayList<FastFieldElement> fastFieldElements = new ArrayList<>();
        for (Element element : fastTypeElement.getEnclosedElements()) {
            if (element.getAnnotation(FastField.class) != null) {
                fastFieldElements.add(new FastFieldElement(element));
            }
        }
        return fastFieldElements;
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
}
