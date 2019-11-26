package com.exactpro.epfast.annotation.processing;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import java.util.Collection;

final class FastErrorReporter implements EnvironmentValidationReporter {
    private final Messager messager;

    private boolean errorRaised = false;

    private static final String INVALID_PACKAGE_TEMPLATE = "FAST package name '%s' is invalid.";

    private static final String DUPLICATE_PACKAGE_TEMPLATE =
        "Multiple @FastPackage annotations referring FAST package %s are found.";

    private static final String DUPLICATE_TYPE_TEMPLATE =
        "Multiple @FastType annotations referring FAST type '%s' are found.";

    private static final String SERVICE_ERROR_TEMPLATE =
        "Unable to create or update java services file for %s service.";

    private static final String PROCESSING_AFTER_FIRST_ROUND_TEMPLATE =
        "FAST annotations processor can not handle classes with @FastPackage, @FastType or @FastField annotations"
            + " added after first processing round. It may be caused by activity of other annotation processors.";

    private static final String ANONYMOUS_EQUALS_NAMED_TEMPLATE =
        "Both anonymous and named package(s) refer to FAST package %s.";

    private static final String DEFAULT_CONSTRUCTOR_ERROR_TEMPLATE =
        "Class annotated with @FastType should be instantiable with default constructor";

    FastErrorReporter(Messager messager) {
        this.messager = messager;
    }

    boolean errorRaised() {
        return errorRaised;
    }

    @Override
    public void reportInvalidPackageName(FastPackageElement fastPackage) {
        errorRaised = true;
        messager.printMessage(Diagnostic.Kind.ERROR,
            String.format(INVALID_PACKAGE_TEMPLATE, fastPackage.getPackageName()),
            ((NamedFastPackage) fastPackage).getPackageElement());
    }

    @Override
    public void reportDuplicatePackages(Collection<FastPackageElement> fastPackages) {
        errorRaised = true;
        fastPackages.forEach(this::reportDuplicatePackage);
    }

    @Override
    public void reportDuplicateTypes(Collection<FastTypeElement> fastTypes) {
        errorRaised = true;
        fastTypes.forEach(this::reportDuplicateType);
    }

    @Override
    public void reportFastTypeNotInstantiable(Element element) {
        errorRaised = true;
        messager.printMessage(Diagnostic.Kind.ERROR, DEFAULT_CONSTRUCTOR_ERROR_TEMPLATE, element);
    }

    void reportProcessingAfterFirstRound() {
        errorRaised = true;
        messager.printMessage(Diagnostic.Kind.ERROR, PROCESSING_AFTER_FIRST_ROUND_TEMPLATE);
    }

    void reportServiceUpdate(TypeName serviceTypeName) {
        errorRaised = true;
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(SERVICE_ERROR_TEMPLATE, serviceTypeName));
    }

    void reportIOException(Exception e) {
        errorRaised = true;
        messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
    }

    private void reportDuplicatePackage(FastPackageElement fastPackage) {
        if (fastPackage instanceof NamedFastPackage) {
            messager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(DUPLICATE_PACKAGE_TEMPLATE, fastPackage.getPackageName()),
                ((NamedFastPackage) fastPackage).getPackageElement()
            );
        } else {
            messager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(ANONYMOUS_EQUALS_NAMED_TEMPLATE, fastPackage.getPackageName())
            );
        }
    }

    private void reportDuplicateType(FastTypeElement fastType) {
        messager.printMessage(Diagnostic.Kind.ERROR,
            String.format(DUPLICATE_TYPE_TEMPLATE, fastType.getTypeName()),
            fastType.getElement());
    }

}
