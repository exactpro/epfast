package com.exactpro.epfast.annotation.processing;

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
        return getFastPackageOf(elementUtils.getPackageElement(getParentPackage(aPackage.toString())));
    }

    private static String getParentPackage(String packageString) {
        int lastDotIndex = packageString.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return "";
        }
        return packageString.substring(0, lastDotIndex);
    }
}
