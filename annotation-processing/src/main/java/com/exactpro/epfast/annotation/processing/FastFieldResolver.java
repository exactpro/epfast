package com.exactpro.epfast.annotation.processing;

import com.exactpro.epfast.annotations.FastField;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import java.util.*;
import java.util.stream.Collectors;

class FastFieldResolver {
    //Stores List of Fast fields for class elements
    private HashMap<Element, List<FastFieldElement>> cache = new HashMap<>();

    private Elements elements;

    public FastFieldResolver(Elements elements) {
        this.elements = elements;
    }

    private boolean isOverriddenIn(FastTypeElement fastType, FastFieldElement fastField) {
        List<FastFieldElement> fastFieldElements = getFastFieldsFor(fastType.getElement());
        return fastFieldElements.stream().anyMatch(field -> field.getMethodName().equals(fastField.getMethodName()) &&
            field.getParameterType().equals(fastField.getParameterType()));
    }

    private List<FastFieldElement> getFastFieldsFor(Element aClass) {
        return cache.computeIfAbsent(aClass,
            key -> key.getEnclosedElements().stream()
            .filter(element -> element.getAnnotation(FastField.class) != null)
            .map(element -> new FastFieldElement((ExecutableElement) element))
            .collect(Collectors.toList()));
    }

    private Element getSuperClass(Element element) {
        TypeMirror superClass = ((TypeElement) element).getSuperclass();
        return elements.getTypeElement(superClass.toString());
    }

    public void resolveFields(FastTypeElement fastType) {
        Element currentType = fastType.getElement();
        Element superType = getSuperClass(currentType);
        while (superType != null) {
            List<FastFieldElement> fastFieldElements = getFastFieldsFor(superType);
            fastFieldElements.stream()
                .filter(fastField -> !isOverriddenIn(fastType, fastField))
                .forEach(fastType::addFastField);
            superType = getSuperClass(superType);
        }
        getFastFieldsFor(currentType).forEach(fastType::addFastField);
    }
}
