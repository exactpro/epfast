package com.exactpro.epfast;

import javax.lang.model.element.PackageElement;
import javax.lang.model.util.Elements;

public class FastPackageHelper {
    Elements elements;

    public FastPackageHelper(Elements elements) {
        this.elements = elements;
    }

    public PackageElement getPackageElement(CharSequence packageName) {
        return elements.getPackageElement(elements.getModuleElement(packageName), packageName);
    }
}
