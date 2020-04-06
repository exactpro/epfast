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
