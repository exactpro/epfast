package com.exactpro.epfast.annotation.processing;

import com.exactpro.epfast.annotations.FastType;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@SupportedAnnotationTypes("com.exactpro.epfast.annotations.FastType")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class FastProcessor extends AbstractProcessor {
    private static final TypeName SERVICE_TYPENAME = new TypeName("com.exactpro.epfast.ICreator");

    private static final TypeName SERVICE_PROVIDER = new TypeName(
        "com.exactpro.epfast.annotation.internal.CreatorImpl");

    private MustacheFactory mustacheFactory = new DefaultMustacheFactory();

    private HashMap<String, Element> fastTypesByName;

    private TypeElement fastTypeElement;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        fastTypeElement = processingEnv.getElementUtils().getTypeElement(FastType.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            try {
                addServiceEntry(SERVICE_TYPENAME, SERVICE_PROVIDER,
                    fastTypesByName.values().stream().toArray(Element[]::new));
            } catch (IOException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    String.format("Unable to create or update java services file for %s service", SERVICE_TYPENAME));
            }
        }
        if (annotations.contains(fastTypeElement)) {
            if (fastTypesByName != null) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    "@FastType annotation processor can not handle classes with @FastType " +
                        "annotation added after first processing round. " +
                        "It may be caused by activity of other annotation processors.");
            }
            fastTypesByName = collectFastTypes(roundEnv);
            try {
                buildCreatorClass(fastTypesByName);
            } catch (Exception e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
            }
            return true;
        }

        return false;
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

    private void buildCreatorClass(HashMap<String, Element> nameClassMap) throws IOException {
        // mustache files are in UTF-8 by default
        Mustache mustache = mustacheFactory.compile(
            "com/exactpro/epfast/annotation/processing/CreatorImpl.java.mustache");
        Collection<Element> elements = nameClassMap.values();
        JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(SERVICE_PROVIDER.toString());
        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
            mustache.execute(out, new CreatorTemplateParameters(SERVICE_PROVIDER, nameClassMap.entrySet())).flush();
        }
    }

    private void addServiceEntry(TypeName service, TypeName provider,
                                 Element... originatingElements) throws IOException {
        Set<TypeName> allProviders = getServiceProviders(service);
        if (allProviders.contains(provider)) {
            return;
        }
        allProviders.add(provider);
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

    private Set<TypeName> getServiceProviders(TypeName service) throws IOException {
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
