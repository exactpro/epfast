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
