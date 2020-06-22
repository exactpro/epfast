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

    private static final String DUPLICATE_FIELD_TEMPLATE =
        "Multiple @FastField annotations referring FAST field '%s' are found.";

    private static final String INVALID_FIELD_TEMPLATE = "@FastField must be a valid java bean setter." +
            " Method must be public, name must start with set, must have exactly 1 parameter";

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

    @Override
    public void reportInvalidFastField(Element fastFieldElement) {
        errorRaised = true;
        messager.printMessage(Diagnostic.Kind.ERROR, INVALID_FIELD_TEMPLATE, fastFieldElement);
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

    @Override
    public void reportDuplicateFields(Collection<FastFieldElement> fastFields) {
        errorRaised = true;
        fastFields.forEach(this::reportDuplicateField);
    }

    private void reportDuplicateField(FastFieldElement fastField) {
        messager.printMessage(Diagnostic.Kind.ERROR,
            String.format(DUPLICATE_FIELD_TEMPLATE, fastField.getFieldName()),
            fastField.getFastField());
    }
}
