package com.exactpro.epfast.annotation.processing;

import java.util.Collection;

interface EnvironmentValidationReporter {
    void reportInvalidPackageName(FastPackageElement fastPackage);

    void reportDuplicatePackages(Collection<FastPackageElement> fastPackages);

    void reportDuplicateTypes(Collection<FastTypeElement> fastTypes);

    void reportDuplicateFields(Collection<FastFieldElement> fastFields);

}
