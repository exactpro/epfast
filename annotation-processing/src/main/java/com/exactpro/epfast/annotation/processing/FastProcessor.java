package com.exactpro.epfast.annotation.processing;

import com.exactpro.epfast.ICreator;
import com.exactpro.epfast.annotation.processing.helpers.FastPackageNameEncoder;
import com.exactpro.epfast.annotations.FastPackage;
import com.exactpro.epfast.annotations.FastType;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@SupportedAnnotationTypes({"com.exactpro.epfast.annotations.FastType", "com.exactpro.epfast.annotations.FastPackage"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class FastProcessor extends AbstractProcessor {
    private static final TypeName SERVICE_TYPENAME = new TypeName(ICreator.class.getTypeName());

    private MustacheFactory mustacheFactory = new DefaultMustacheFactory();

    private TypeElement fastTypeElement;

    private TypeElement fastPackageElement;

    private Elements elementUtils;

    private Filer filer;

    private FastEnvironment fastEnvironment;

    private FastErrorReporter errorReporter;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.elementUtils = processingEnv.getElementUtils();
        this.filer = processingEnv.getFiler();
        this.fastTypeElement = elementUtils.getTypeElement(FastType.class.getCanonicalName());
        this.fastPackageElement = elementUtils.getTypeElement(FastPackage.class.getCanonicalName());
        Messager messager = processingEnv.getMessager();
        this.errorReporter = new FastErrorReporter(messager);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        boolean annotationsClaimed = false;

        if (annotations.contains(fastTypeElement) || annotations.contains(fastPackageElement)) {
            annotationsClaimed = true;

            if (fastEnvironment != null) {
                errorReporter.reportProcessingAfterFirstRound();
            }
            fastEnvironment = FastEnvironment.build(roundEnv, elementUtils);
            fastEnvironment.validate(errorReporter);
            if (!errorReporter.errorRaised()) {
                try {
                    buildCreatorClasses(fastEnvironment);
                } catch (IOException e) {
                    errorReporter.reportIOException(e);
                }
            }
        }

        if (roundEnv.processingOver() && !errorReporter.errorRaised()) {
            HashSet<TypeName> creatorImplTypeNames = getNewServiceProviders(fastEnvironment);
            HashSet<Element> originatingElements = new HashSet<>();
            fastEnvironment.getFastPackages().forEach(packageElement ->
                originatingElements.addAll(packageElement.getFastTypes().stream()
                    .map(FastTypeElement::getElement)
                    .collect(Collectors.toList())));
            try {
                FastServiceCreator fastServiceCreator = new FastServiceCreator(processingEnv);
                fastServiceCreator.updateServiceEntries(SERVICE_TYPENAME, creatorImplTypeNames,
                    originatingElements.stream().toArray(Element[]::new));
            } catch (IOException e) {
                errorReporter.reportServiceUpdate(SERVICE_TYPENAME);
            }
        }

        return annotationsClaimed;
    }

    private void buildCreatorClasses(FastEnvironment fastEnvironment) throws IOException {
        Mustache mustache = mustacheFactory.compile(
            "com/exactpro/epfast/annotation/processing/FieldSetter.java.mustache");
        for (FastPackageElement fastPackage : fastEnvironment.getFastPackages()) {
            for (FastTypeElement fastTypeElement : fastPackage.getFastTypes()) {
                TypeName typeName = fastTypeElement.getFieldSetterTypeName();
                JavaFileObject fieldSetterFile = filer.createSourceFile(typeName.toString());
                try (PrintWriter out = new PrintWriter(fieldSetterFile.openWriter())) {
                    mustache.execute(out, fastTypeElement);
                }
            }
        }

        for (FastPackageElement packageElement : fastEnvironment.getFastPackages()) {
            TypeName typeName = new TypeName(String.format("com.exactpro.epfast.annotation.internal.%s.CreatorImpl",
                FastPackageNameEncoder.encode(packageElement.getPackageName())));
            // mustache files are in UTF-8 by default
            mustache = mustacheFactory.compile(
                "com/exactpro/epfast/annotation/processing/CreatorImpl.java.mustache");
            JavaFileObject builderFile = filer.createSourceFile(typeName.toString());
            try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
                mustache.execute(out, new CreatorTemplateParameters(
                    typeName, packageElement));
            }
        }
    }

    private HashSet<TypeName> getNewServiceProviders(FastEnvironment fastEnvironment) {
        HashSet<TypeName> creatorTypeNames = new HashSet<>();
        for (FastPackageElement entry : fastEnvironment.getFastPackages()) {
            if (entry.getFastTypes().isEmpty()) {
                continue;
            }
            String packageName = FastPackageNameEncoder.encode(entry.getPackageName());
            creatorTypeNames.add(new TypeName(
                String.format("com.exactpro.epfast.annotation.internal.%s.CreatorImpl", packageName)));
        }
        return creatorTypeNames;
    }

}
