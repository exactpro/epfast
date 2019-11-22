package com.exactpro.epfast.annotation.processing;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import java.util.Arrays;
import java.util.stream.Collectors;

public class AnonymousFastPackage extends FastPackageElement {
    private String rootPackageName;

    @Override
    public void addFastType(FastTypeElement fastTypeElement) {
        super.addFastType(fastTypeElement);
        Element element = fastTypeElement.getElement();
        do {
            element = element.getEnclosingElement();
        } while (element.getKind() != ElementKind.PACKAGE);

        rootPackageName = findCommonParent(rootPackageName, ((PackageElement) element).getQualifiedName().toString());
    }

    @Override
    public boolean hasValidPackageName() {
        return true;
    }

    @Override
    public String getPackageName() {
        return rootPackageName;
    }

    private static String findCommonParent(String rootPath, String javaPackage) {
        if (rootPath == null) {
            return javaPackage;
        }
        return getDeepestCommonPackagePath(rootPath, javaPackage);
    }

    static String getDeepestCommonPackagePath(String a, String b) {
        String[] aPackages = a.split("\\.");
        String[] bPackages = b.split("\\.");
        int limit = Math.min(aPackages.length, bPackages.length);
        int numCommonSegments = 0;
        while (numCommonSegments < limit && aPackages[numCommonSegments].equals(bPackages[numCommonSegments])) {
            ++numCommonSegments;
        }
        return Arrays.stream(aPackages).limit(numCommonSegments).collect(Collectors.joining("."));
    }
}
