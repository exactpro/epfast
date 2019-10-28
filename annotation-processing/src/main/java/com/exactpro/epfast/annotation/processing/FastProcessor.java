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
import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@SupportedAnnotationTypes({"com.exactpro.epfast.annotations.FastType", "com.exactpro.epfast.annotations.FastPackage"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class FastProcessor extends AbstractProcessor {
    private static final TypeName SERVICE_TYPENAME = new TypeName("com.exactpro.epfast.ICreator");

    private MustacheFactory mustacheFactory = new DefaultMustacheFactory();

    private TypeElement fastTypeElement;

    private TypeElement fastPackageElement;

    private HashMap<PackageElement, HashMap<String, Element>> packageElementsMap;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        fastTypeElement = processingEnv.getElementUtils().getTypeElement(FastType.class.getCanonicalName());
        fastPackageElement = processingEnv.getElementUtils().getTypeElement(FastPackage.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            HashSet<TypeName> creatorImplTypeNames = getNewServiceProviders(packageElementsMap);
            HashSet<Element> originatingElements = new HashSet<>();
            packageElementsMap.values().forEach(elements->originatingElements.addAll(elements.values()));
            try {
                addServiceEntries(SERVICE_TYPENAME, creatorImplTypeNames,
                    originatingElements.stream().toArray(Element[]::new));
            } catch (IOException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    String.format("Unable to create or update java services file for %s service", SERVICE_TYPENAME));
            }
        }

        if (annotations.contains(fastTypeElement)) {
            if (packageElementsMap != null) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    "@FastType annotation processor can not handle classes with @FastType " +
                        "annotation added after first processing round. " +
                        "It may be caused by activity of other annotation processors.");
            }
            packageElementsMap = collectFastPackages(roundEnv);
            HashMap<String, Element> fastTypesByName = collectFastTypes(roundEnv);
            organizeInPackages(fastTypesByName);
            try {
                buildCreatorClass(packageElementsMap);
            } catch (Exception e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
            }
            return true;
        }
        return false;
    }

    private void organizeInPackages(HashMap<String, Element> fastTypesByName) {
        for (Map.Entry<String, Element> entry : fastTypesByName.entrySet()) {
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
        HashMap<String, ArrayList<Element>> fastTypesByName = new HashMap<>();
        Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(FastType.class);
        for (Element element : annotatedElements) {
            String fastTypeName = element.getAnnotation(FastType.class).name();
            if (fastTypeName.isEmpty()) {
                fastTypeName = element.getSimpleName().toString();
            }
            fastTypesByName.computeIfAbsent(fastTypeName, key -> new ArrayList<>()).add(element);
        }
        return filterDuplicates(fastTypesByName);
    }

    private HashMap<String, Element> filterDuplicates(HashMap<String, ArrayList<Element>> fastTypesByName) {
        HashMap<String, Element> filteredFastTypesByName = new HashMap<>();
        for (Map.Entry<String, ArrayList<Element>> entry : fastTypesByName.entrySet()) {
            String fastTypeName = entry.getKey();
            ArrayList<Element> elements = entry.getValue();
            Element element = getSingleElementOrNull(elements);
            if (element == null) {
                reportDuplicates(fastTypeName, elements);
            } else {
                filteredFastTypesByName.put(fastTypeName, element);
            }
        }
        return filteredFastTypesByName;
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

    private HashSet<TypeName> getNewServiceProviders(
        HashMap<PackageElement, HashMap<String, Element>> packageElementMap) {

        HashSet<TypeName> creatorTypeNames = new HashSet<>();
        for (Map.Entry<PackageElement, HashMap<String, Element>> entry : packageElementMap.entrySet()) {
            if (entry.getValue().isEmpty()) {
                continue;
            }
            String packageName = entry.getKey().isUnnamed() ? "" : String.format("%s.", entry.getKey());
            creatorTypeNames.add(new TypeName(
                String.format("com.exactpro.epfast.annotation.internal.packages.%sCreatorImpl", packageName)));
        }
        return creatorTypeNames;
    }

    private void addServiceEntries(TypeName service, HashSet<TypeName> providers,
                                 Element... originatingElements) throws IOException {
        Set<TypeName> allProviders = getExistingServiceProviders(service);
        if (allProviders.containsAll(providers)) {
            return;
        }
        allProviders.addAll(providers);
        FileObject resourceFile = processingEnv.getFiler().createResource(
            StandardLocation.CLASS_OUTPUT,
            "",
            getServicesFilePath(service),
            originatingElements);
        try (PrintWriter out = new PrintWriter(resourceFile.openOutputStream())) {
            for (TypeName serviceProvider : allProviders) {
                out.println(serviceProvider);
            }
        }
    }

    private Set<TypeName> getExistingServiceProviders(TypeName service) throws IOException {
        HashSet<TypeName> serviceProviders = new HashSet<>();
        FileObject existingFile = processingEnv.getFiler().getResource(
            StandardLocation.CLASS_OUTPUT,
            "",
            getServicesFilePath(service));

        try (BufferedReader br = new BufferedReader(new InputStreamReader(existingFile.openInputStream()))) {
            return br.lines().filter(provider->!provider.isEmpty())
                .map(TypeName::new).collect(Collectors.toSet());
        } catch (FileNotFoundException e) {
            //resource didn't exist before, so we ignore it
        }
        return serviceProviders;
    }

    private String getServicesFilePath(TypeName service) {
        return Paths.get("META-INF", "services", service.toString()).toString();
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
