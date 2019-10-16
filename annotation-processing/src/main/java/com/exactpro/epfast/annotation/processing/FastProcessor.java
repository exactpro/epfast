package com.exactpro.epfast.annotation.processing;

import com.exactpro.epfast.annotations.FastPackage;
import com.exactpro.epfast.annotations.FastType;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@SupportedAnnotationTypes({"com.exactpro.epfast.annotations.FastType", "com.exactpro.epfast.annotations.FastPackage"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class FastProcessor extends AbstractProcessor {
    private MustacheFactory mustacheFactory = new DefaultMustacheFactory();

    private TypeElement fastTypeElement;

    private TypeElement fastPackageElement;

    private HashMap<PackageElement, ArrayList<Element>> packageElementsMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        fastTypeElement = processingEnv.getElementUtils().getTypeElement(FastType.class.getCanonicalName());
        fastPackageElement = processingEnv.getElementUtils().getTypeElement(FastPackage.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.contains(fastTypeElement)) {
            HashMap<PackageElement, HashMap<String, Element>> packageElementsMap = collectFastPackages(roundEnv);
            HashMap<String, Element> nameClassMap = collectFastTypes(roundEnv);
            organizeInPackages(packageElementsMap, nameClassMap);
            try {
                buildCreatorClass(packageElementsMap);
            } catch (Exception e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
            }
            return true;
        }

        return false;
    }

    private void organizeInPackages(HashMap<PackageElement, HashMap<String, Element>> packageElementsMap,
                                    HashMap<String, Element> nameClassMap) {
        for (Map.Entry<String, Element> entry : nameClassMap.entrySet()) {
            PackageElement enclosingPackage = processingEnv.getElementUtils().getPackageOf(entry.getValue());
            while (!packageElementsMap.containsKey(enclosingPackage)) {
                enclosingPackage = getParent(enclosingPackage);
            }
            packageElementsMap.get(enclosingPackage).put(entry.getKey(), entry.getValue());
        }
    }

    private PackageElement getParent(Element enclosing) {
        String currentPackage = enclosing.toString();
        int lastDotIndex = currentPackage.lastIndexOf('.');
        if (lastDotIndex  == -1) {
            return processingEnv.getElementUtils().getPackageElement("");
        }
        return processingEnv.getElementUtils().getPackageElement(currentPackage.substring(0, lastDotIndex));
    }

    private HashMap<PackageElement, HashMap<String, Element>> collectFastPackages(RoundEnvironment roundEnv) {
        HashMap<PackageElement, HashMap<String, Element>> packageElementMap = new HashMap<>();
        PackageElement topPackage = processingEnv.getElementUtils().getPackageElement("");
        packageElementMap.put(topPackage, new HashMap<>());
        Set<? extends Element> packageSet = roundEnv.getElementsAnnotatedWith(FastPackage.class);
        for (Element element : packageSet) {
            String packageName = element.getAnnotation(FastPackage.class).name();
            if (packageName.isEmpty()) {
                PackageElement packageElement = processingEnv.getElementUtils().getPackageOf(element);
                packageElementMap.put(packageElement, new HashMap<>());
            } else {
                try {
                    PackageElement packageElement = processingEnv.getElementUtils().getPackageElement(packageName);
                    packageElementMap.put(packageElement, new HashMap<>());
                } catch (Exception e) {
                    processingEnv.getMessager().printMessage(
                        Diagnostic.Kind.ERROR,
                        String.format("Package for FastPackage name = %s doesn't exist", packageName),
                        element);
                }
            }
        }
        return packageElementMap;
    }

    private HashMap<String, Element> collectFastTypes(RoundEnvironment roundEnv) {
        HashMap<String, ArrayList<Element>> nameClassMap = new HashMap<>();
        Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(FastType.class);
        for (Element element : annotatedElements) {
            String fastTypeName = element.getAnnotation(FastType.class).name();
            if (fastTypeName.isEmpty()) {
                fastTypeName = element.getSimpleName().toString();
            }
            nameClassMap.computeIfAbsent(fastTypeName, key -> new ArrayList<>()).add(element);
        }
        return filterDuplicates(nameClassMap);
    }

    private HashMap<String, Element> filterDuplicates(HashMap<String, ArrayList<Element>> nameClassMap) {
        HashMap<String, Element> filteredNameClassMap = new HashMap<>();
        for (Map.Entry<String, ArrayList<Element>> entry : nameClassMap.entrySet()) {
            String fastTypeName = entry.getKey();
            ArrayList<Element> elements = entry.getValue();
            Element element = getSingleElementOrNull(elements);
            if (element == null) {
                reportDuplicates(fastTypeName, elements);
            } else {
                filteredNameClassMap.put(fastTypeName, element);
            }
        }
        return filteredNameClassMap;
    }

    private void reportDuplicates(String fastTypeName, Collection<Element> elements) {
        Messager messager = processingEnv.getMessager();
        elements.forEach(element -> messager.printMessage(
            Diagnostic.Kind.ERROR,
            String.format("Multiple declarations referring to FastType '%s' are found.", fastTypeName),
            element));
    }

    private void buildCreatorClass(HashMap<PackageElement,
        HashMap<String, Element>> packageElementMap) throws IOException {

        HashSet<String> creatorImplTypeNames = getCreatorImplTypeNames(packageElementMap);
        createResourceFile(creatorImplTypeNames);
        for (Map.Entry<PackageElement, HashMap<String, Element>> entry : packageElementMap.entrySet()) {
            if (entry.getValue().isEmpty()) {
                continue;
            }
            String packageName = entry.getKey().isUnnamed() ? "" : String.format("%s.", entry.getKey());
            TypeName typeName = new TypeName(
                String.format("com.exactpro.epfast.annotation.internal.packages.%sCreatorImpl", packageName));
            // mustache files are in UTF-8 by default
            Mustache mustache = mustacheFactory.compile(
                "com/exactpro/epfast/annotation/processing/CreatorImpl.java.mustache");

            JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(typeName.toString());
            try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
                mustache.execute(out, new CreatorTemplateParameters(typeName, entry.getValue().entrySet())).flush();
            }
        }
    }

    private HashSet<String> getCreatorImplTypeNames(
        HashMap<PackageElement, HashMap<String, Element>> packageElementMap) {

        HashSet<String> creatorTypeNames = new HashSet<>();
        for (Map.Entry<PackageElement, HashMap<String, Element>> entry : packageElementMap.entrySet()) {
            if (entry.getValue().isEmpty()) {
                continue;
            }
            String packageName = entry.getKey().isUnnamed() ? "" : String.format("%s.", entry.getKey());
            creatorTypeNames.add(packageName);
        }
        return creatorTypeNames;
    }

    private void createResourceFile(HashSet<String> packageNames) throws IOException {
        FileObject resourceFile = processingEnv.getFiler().createResource(
            StandardLocation.CLASS_OUTPUT,
            "",
            "META-INF" + File.separator + "services" + File.separator + "com.exactpro.epfast.ICreator"
            );
        try (PrintWriter out = new PrintWriter(resourceFile.openOutputStream())) {
            for (String packageName : packageNames) {
                out.write(
                    String.format("com.exactpro.epfast.annotation.internal.packages.%sCreatorImpl\n", packageName));
            }
        }
    }

    private static <T> T getSingleElementOrNull(Collection<T> collection) {
        Iterator<T> iterator = collection.iterator();
        if (!iterator.hasNext()) {
            return null;
        }
        T value = iterator.next();
        return iterator.hasNext() ? null : value;
    }

}
