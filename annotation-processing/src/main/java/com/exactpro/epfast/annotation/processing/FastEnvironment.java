package com.exactpro.epfast.annotation.processing;

import com.exactpro.epfast.annotations.FastPackage;
import com.exactpro.epfast.annotations.FastType;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.util.*;
import java.util.stream.Collectors;

public class FastEnvironment {

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

    public static FastEnvironment build(RoundEnvironment environment, Elements elements) {
        final FastPackageResolver packageResolver = new FastPackageResolver(elements);
        final FastFieldsResolver fieldsResolver = new FastFieldsResolver(elements);

        getFastPackages(environment).forEach(packageResolver::registerFastPackage);
        getFastTypes(environment).forEach(
            typeElement -> packageResolver.getFastPackageOf(elements.getPackageOf(typeElement))
                .addFastType(new FastTypeElement(typeElement)));
        packageResolver.getKnownFastPackages().forEach(fastPackage -> fastPackage.getFastTypes()
            .forEach(fieldsResolver::resolveFields));
        fieldsResolver.inheritFields();
        return new FastEnvironment(packageResolver.getKnownFastPackages());
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
                    validateInstantiable(fastType);
                    validateFastFieldDuplicates(fastType.getFastFields());
                });
            });
        }

        private void validateFastFieldDuplicates(List<FastFieldElement> fastFields) {
            HashMap<String, ArrayList<FastFieldElement>> fastFieldNames = new HashMap<>();
            fastFields.forEach(fastField ->
                fastFieldNames.computeIfAbsent(fastField.getFieldName(), key -> new ArrayList<>())
                    .add(fastField));
            fastFieldNames.values().stream().filter(fastFieldElements -> fastFieldElements.size() > 1)
                .forEach(reporter::reportDuplicateFields);
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
