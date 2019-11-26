package com.exactpro.epfast.annotation.processing;

import com.exactpro.epfast.annotations.FastField;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import java.util.*;

class FastFieldsResolver {
    private ArrayList<FastTypeElement> cache = new ArrayList<>();

    private Elements elements;

    public FastFieldsResolver(Elements elements) {
        this.elements = elements;
    }

    private boolean isOverrideMethod(List<FastFieldElement> fastFields, String methodName) {
        return fastFields.stream().anyMatch(fastField -> fastField.getMethodName().equals(methodName));
    }

    private ArrayList<FastFieldElement> getFastFieldsFor(Element superClass) {
        ArrayList<FastFieldElement> fastFields = new ArrayList<>();
        for (Element fastFieldElement : superClass.getEnclosedElements()) {
            if (fastFieldElement.getAnnotation(FastField.class) != null) {
                fastFields.add(new FastFieldElement(fastFieldElement));
            }
        }
        return fastFields;
    }

    private Element getSuperClass(Element element) {
        TypeMirror superClass = ((TypeElement) element).getSuperclass();
        return elements.getTypeElement(superClass.toString());
    }

    public void resolveFields(FastTypeElement fastType) {
        cache.add(fastType);
        fastType.getElement().getEnclosedElements().stream()
                .filter(element -> element.getAnnotation(FastField.class) != null)
                .forEach(element -> fastType.addFastField(new FastFieldElement(element)));
    }

    public void inheritFields() {
        for (FastTypeElement fastType : cache) {
            Element currentType = fastType.getElement();
            while (true) {
                currentType = getSuperClass(currentType);
                if (currentType == null) {
                    break;
                }
                ArrayList<FastFieldElement> fastFieldElements = getFastFieldsFor(currentType);
                fastFieldElements.stream()
                    .filter(fastField -> !isOverrideMethod(fastType.getFastFields(), fastField.getMethodName()))
                    .forEach(fastType::addFastField);
            }
        }
    }
}
