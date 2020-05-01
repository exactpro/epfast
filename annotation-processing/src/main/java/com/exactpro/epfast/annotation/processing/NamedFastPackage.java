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

import com.exactpro.epfast.annotations.FastPackage;

import javax.lang.model.element.PackageElement;
import java.util.regex.Pattern;

public class NamedFastPackage extends FastPackageElement {

    private final String fastPackageName;

    private final PackageElement packageElement;

    private static final String FAST_PACKAGE_PATTERN_REGEX =
        "[$_A-Za-z][$_A-Za-z0-9]*|[$_A-Za-z][$_A-Za-z0-9]*(\\.[$_A-Za-z][$_A-Za-z0-9]*)*|^$";

    private static final Pattern FAST_PACKAGE_PATTERN
        = Pattern.compile(FAST_PACKAGE_PATTERN_REGEX);

    public NamedFastPackage(PackageElement packageElement) {
        this.packageElement = packageElement;
        this.fastPackageName = getNameFrom(packageElement);
    }

    @Override
    public boolean hasValidPackageName() {
        return FAST_PACKAGE_PATTERN.matcher(fastPackageName).matches();
    }

    @Override
    public String getPackageName() {
        return fastPackageName;
    }

    public PackageElement getPackageElement() {
        return packageElement;
    }

    private static String getNameFrom(PackageElement packageElement) {
        String fastPackageName = packageElement.getAnnotation(FastPackage.class).name();
        if (fastPackageName.isEmpty()) {
            return packageElement.getQualifiedName().toString();
        }
        return fastPackageName;
    }

    static boolean isValidPackage(String packageName) {
        return FAST_PACKAGE_PATTERN.matcher(packageName).matches();
    }
}
