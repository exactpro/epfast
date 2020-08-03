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

import com.exactpro.epfast.FastPackageHelper;
import com.exactpro.epfast.annotations.FastPackage;

import javax.lang.model.element.PackageElement;
import javax.lang.model.util.Elements;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

class FastPackageResolver {
    private final Elements elementUtils;

    private HashMap<PackageElement, FastPackageElement> cache = new HashMap<>();

    private AnonymousFastPackage anonymousPackage = new AnonymousFastPackage();

    FastPackageResolver(Elements elementUtils) {
        this.elementUtils = elementUtils;
    }

    FastPackageElement getFastPackageOf(PackageElement aPackage) {
        return cache.computeIfAbsent(aPackage, this::computeFastPackageOf);
    }

    void registerFastPackage(PackageElement aPackage) {
        cache.computeIfAbsent(aPackage, NamedFastPackage::new);
    }

    Collection<FastPackageElement> getKnownFastPackages() {
        // Collecting unique objects
        TreeSet<FastPackageElement> allPackages = new TreeSet<>(Comparator.comparingInt(System::identityHashCode));
        allPackages.addAll(cache.values());
        return allPackages;
    }

    private FastPackageElement computeFastPackageOf(PackageElement aPackage) {
        FastPackage fastPackage = aPackage.getAnnotation(FastPackage.class);
        if (fastPackage != null) {
            return new NamedFastPackage(aPackage);
        }
        if (aPackage.isUnnamed()) { // if the root java package
            return anonymousPackage;
        }
        return getFastPackageOf(new FastPackageHelper(elementUtils).getPackageElement(getParentPackage(aPackage.toString())));
    }

    private static String getParentPackage(String packageString) {
        int lastDotIndex = packageString.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return "";
        }
        return packageString.substring(0, lastDotIndex);
    }
}
