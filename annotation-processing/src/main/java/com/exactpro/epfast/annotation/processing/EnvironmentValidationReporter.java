package com.exactpro.epfast.annotation.processing;

import javax.lang.model.element.Element;
import java.util.Collection;

interface EnvironmentValidationReporter {
    void reportInvalidPackageName(FastPackageElement fastPackage);

    void reportDuplicatePackages(Collection<FastPackageElement> fastPackages);

    void reportDuplicateTypes(Collection<FastTypeElement> fastTypes);

    void reportFastTypeNotInstantiable(Element element);

}
