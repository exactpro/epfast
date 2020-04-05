/******************************************************************************
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
 ******************************************************************************/

package com.exactpro.epfast.annotation.processing;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVisitor;
import javax.lang.model.util.SimpleTypeVisitor8;

public class FastTypeConstructorValidator {
    private static final TypeVisitor<Boolean, Void> hasNoArguments =
        new SimpleTypeVisitor8<Boolean, Void>() {
            public Boolean visitExecutable(ExecutableType t, Void v) {
                return t.getParameterTypes().isEmpty();
            }
        };

    private final Element fastTypeElement;

    public FastTypeConstructorValidator(Element fastTypeElement) {
        this.fastTypeElement = fastTypeElement;
    }

    public boolean hasDefaultConstructor() {
        for (Element childElement : fastTypeElement.getEnclosedElements()) {
            if (childElement.getKind() == ElementKind.CONSTRUCTOR &&
                childElement.getModifiers().contains(Modifier.PUBLIC)) {
                TypeMirror mirror = childElement.asType();
                if (mirror.accept(hasNoArguments, null)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isInnerClass() {
        return fastTypeElement.getEnclosingElement().getKind().isClass();
    }

    public boolean isPrivateClass() {
        return !fastTypeElement.getModifiers().contains(Modifier.PUBLIC);
    }

    public boolean isAbstract() {
        return fastTypeElement.getModifiers().contains(Modifier.ABSTRACT);
    }
}
