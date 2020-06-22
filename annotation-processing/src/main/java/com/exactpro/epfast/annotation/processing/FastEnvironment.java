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

import com.exactpro.epfast.annotations.FastPackage;
import com.exactpro.epfast.annotations.FastType;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FastEnvironment {
    private static final Set<Modifier> FAST_FIELD_MODIFIERS = Stream.of(Modifier.PUBLIC).collect(Collectors.toSet());

    private Collection<FastPackageElement> fastPackages;

    private FastEnvironment(Collection<FastPackageElement> fastPackages) {
        this.fastPackages = fastPackages;
    }

    public Collection<FastPackageElement> getFastPackages() {
        return fastPackages;
    }

    public void validate(EnvironmentValidationReporter reporter) {
        new Validator(reporter).validate();
    }

    public static FastEnvironment build(RoundEnvironment roundEnv, ProcessingEnvironment processingEnv) {
        final Elements elements = processingEnv.getElementUtils();
        final FastPackageResolver packageResolver = new FastPackageResolver(elements);
        final FastFieldResolver fieldResolver = new FastFieldResolver(processingEnv.getTypeUtils());
        getFastPackages(roundEnv).forEach(packageResolver::registerFastPackage);
        getFastTypes(roundEnv).forEach(
            typeElement -> packageResolver.getFastPackageOf(elements.getPackageOf(typeElement))
                .addFastType(new FastTypeElement(typeElement)));
        Collection<FastPackageElement> knownFastPackages = packageResolver.getKnownFastPackages();
        knownFastPackages.forEach(fastPackage -> fastPackage.getFastTypes()
            .forEach(fieldResolver::resolveFields));
        return new FastEnvironment(knownFastPackages);
    }

    @SuppressWarnings("unchecked")
    private static Collection<PackageElement> getFastPackages(RoundEnvironment roundEnv) {
        return (Set<PackageElement>) roundEnv.getElementsAnnotatedWith(FastPackage.class);
    }

    @SuppressWarnings("unchecked")
    private static Collection<TypeElement> getFastTypes(RoundEnvironment roundEnv) {
        return (Set<TypeElement>) roundEnv.getElementsAnnotatedWith(FastType.class);
    }

    private class Validator {
        private EnvironmentValidationReporter reporter;

        Validator(EnvironmentValidationReporter reporter) {
            this.reporter = reporter;
        }

        void validate() {
            validatePackageNames();
            validatePackageNameDuplicates();
            getFastPackages().forEach(it -> {
                validateTypeNameDuplicates(it);
                it.getFastTypes().forEach(fastType -> {
                    List<FastFieldElement> fastFields = fastType.getFastFields();
                    fastFields.forEach(this::validateFastField);
                    validateFastFieldDuplicates(fastFields);
                });
            });
        }

        private void validateFastField(FastFieldElement fastFieldElement) {
            String methodName = fastFieldElement.getMethodName();
            if (!methodName.startsWith("set") || methodName.length() <= 3) {
                reporter.reportInvalidFastField(fastFieldElement.getFastField());
            }
            List<? extends VariableElement> parameters = ((ExecutableElement) fastFieldElement.getFastField())
                .getParameters();
            if (parameters.size() != 1) {
                reporter.reportInvalidFastField(fastFieldElement.getFastField());
            }

            if (!fastFieldElement.getFastField().getModifiers().equals(FAST_FIELD_MODIFIERS)) {
                reporter.reportInvalidFastField(fastFieldElement.getFastField());
            }
        }

        private void validateFastFieldDuplicates(List<FastFieldElement> fastFields) {
            Map<String, List<FastFieldElement>> fastFieldNames
                = fastFields.stream().collect(Collectors.groupingBy(FastFieldElement::getFieldName));
            fastFieldNames.values().stream().filter(fastFieldElements -> fastFieldElements.size() > 1)
                .forEach(reporter::reportDuplicateFields);
            getFastPackages().forEach(it -> {
                validateTypeNameDuplicates(it);
                it.getFastTypes().forEach(this::validateInstantiable);
            });
        }

        private void validateInstantiable(FastTypeElement fastType) {
            Element fastTypeElement = fastType.getElement();
            FastTypeConstructorValidator constructorValidator = new FastTypeConstructorValidator(fastTypeElement);
            if (constructorValidator.isAbstract() ||
                constructorValidator.isInnerClass() ||
                constructorValidator.isPrivateClass() ||
                !constructorValidator.hasDefaultConstructor()) {
                reporter.reportFastTypeNotInstantiable(fastTypeElement);
            }
        }

        private void validatePackageNames() {
            getFastPackages().stream()
                .filter(fastPackage -> !fastPackage.hasValidPackageName())
                .forEach(reporter::reportInvalidPackageName);
        }

        private void validatePackageNameDuplicates() {
            Map<String, List<FastPackageElement>> packagesByName
                = fastPackages.stream().collect(Collectors.groupingBy(FastPackageElement::getPackageName));
            packagesByName.values().stream()
                .filter(packages -> packages.size() > 1)
                .forEach(reporter::reportDuplicatePackages);
        }

        private void validateTypeNameDuplicates(FastPackageElement fastPackage) {
            Map<String, List<FastTypeElement>> typesByName
                = fastPackage.getFastTypes().stream().collect(Collectors.groupingBy(FastTypeElement::getTypeName));
            typesByName.values().stream()
                .filter(types -> types.size() > 1)
                .forEach(reporter::reportDuplicateTypes);
        }
    }

}
