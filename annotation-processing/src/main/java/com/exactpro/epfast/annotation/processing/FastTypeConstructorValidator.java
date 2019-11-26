package com.exactpro.epfast.annotation.processing;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVisitor;
import javax.lang.model.util.SimpleTypeVisitor8;

public class FastTypeConstructorValidator {
    private static final TypeVisitor<Boolean, Void> noArgsVisitor =
        new SimpleTypeVisitor8<Boolean, Void>() {
            public Boolean visitExecutable(ExecutableType t, Void v) {
                return t.getParameterTypes().isEmpty();
            }
        };

    public static boolean acceptsDefaultConstructor(Element element) {

        for (Element childElement : element.getEnclosedElements()) {
            if (childElement.getKind() == ElementKind.CONSTRUCTOR &&
                childElement.getModifiers().contains(Modifier.PUBLIC)) {
                TypeMirror mirror = childElement.asType();
                if (mirror.accept(noArgsVisitor, null)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isInnerClass(Element element) {
        return element.getEnclosingElement().getKind().isClass();
    }

    public static boolean isAbstract(Element element) {
        return element.getModifiers().contains(Modifier.ABSTRACT);
    }
}
