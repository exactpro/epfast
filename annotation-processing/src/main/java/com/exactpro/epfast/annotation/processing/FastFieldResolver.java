/*
 * Copyright 2019-2020 Exactpro (Exactpro Systems Limited)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.exactpro.epfast.annotation.processing;

import com.exactpro.epfast.annotations.FastField;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.*;
import java.util.stream.Collectors;

class FastFieldResolver {
    //Stores List of Fast fields for class elements
    private HashMap<Element, List<FastFieldElement>> cache = new HashMap<>();

    private final Types typeUtils;

    public FastFieldResolver(Types typeUtils) {
        this.typeUtils = typeUtils;
    }

    private boolean isOverridenIn(List<FastFieldElement> currentFastFields, FastFieldElement fastField) {
        List<TypeMirror> fastFieldMethodParameters = getMethodParametersTypeMirrors(fastField);
        return currentFastFields.stream().anyMatch(fastFieldElement -> {
            List<TypeMirror> parameters = getMethodParametersTypeMirrors(fastFieldElement);
            return fastFieldElement.getMethodName().equals(fastField.getMethodName()) &&
                fastFieldMethodParameters.equals(parameters);
        });
    }

    private List<TypeMirror> getMethodParametersTypeMirrors(FastFieldElement fastField) {
        return fastField.getFastField().getParameters()
            .stream().map(Element::asType).collect(Collectors.toList());
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
        return typeUtils.asElement(superClass);
    }

    public void resolveFields(FastTypeElement fastType) {
        Element currentType = fastType.getElement();
        Element superType = getSuperClass(currentType);
        List<FastFieldElement> currentFastFields = getFastFieldsFor(currentType);
        while (superType != null) {
            List<FastFieldElement> fastFieldElements = getFastFieldsFor(superType);
            fastFieldElements.stream()
                .filter(fastField -> !isOverridenIn(currentFastFields, fastField))
                .forEach(currentFastFields::add);
            superType = getSuperClass(superType);
        }
        currentFastFields.forEach(fastType::addFastField);
    }
}
