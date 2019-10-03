package com.exactpro.epfast.annotation.processing;

import com.exactpro.epfast.annotations.FastType;

import javax.lang.model.element.Element;

public class FastTypeElement {
    private String fastTypeName;

    private Element fastTypeElement;

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

    public String getTypeName() {
        return fastTypeName;
    }

    public Element getElement() {
        return fastTypeElement;
    }

}
