package com.exactpro.epfast.annotation.processing;

import com.exactpro.epfast.annotations.FastPackage;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import java.util.*;

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

    void inheritFields() {
        HashSet<FastTypeElement> fastTypes = getAllFastTypes();
        for (FastTypeElement fastType : fastTypes) {
            Element currentType = fastType.getElement();
            while (true) {
                currentType = getSuperClass(currentType);
                if (currentType == null) {
                    break;
                }
                ArrayList<FastFieldElement> fastFieldElements = getFastFieldsFor(currentType);
                fastFieldElements.stream()
                    .filter(fastField -> !isOverrideMethod(fastType.getFastFields(), fastField.getMethodName()))
                    .forEach(fastType::addFastField);
            }
        }
    }

    private boolean isOverrideMethod(List<FastFieldElement> fastFields, String methodName) {
        return fastFields.stream().anyMatch(fastField -> fastField.getMethodName().equals(methodName));
    }

    private HashSet<FastTypeElement> getAllFastTypes() {
        HashSet<FastTypeElement> fastTypes = new HashSet<>();
        cache.values().forEach(fastPackage -> fastTypes.addAll(fastPackage.getFastTypes()));
        return fastTypes;
    }

    private ArrayList<FastFieldElement> getFastFieldsFor(Element superClass) {
        ArrayList<FastFieldElement> fastFields = new ArrayList<>();
        HashSet<FastTypeElement> allFastTypes = getAllFastTypes();
        allFastTypes.stream().filter(fastType -> fastType.getElement().equals(superClass))
            .forEach(fastTypeElement -> fastFields.addAll(fastTypeElement.getFastFields()));

        return fastFields;
    }

    private Element getSuperClass(Element element) {
        TypeMirror superClass = ((TypeElement) element).getSuperclass();
        return elementUtils.getTypeElement(superClass.toString());
    }

}
