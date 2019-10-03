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
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@SupportedAnnotationTypes("com.exactpro.epfast.annotations.FastType")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class FastProcessor extends AbstractProcessor {
    private MustacheFactory mustacheFactory = new DefaultMustacheFactory();

    private TypeElement fastTypeElement;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        fastTypeElement = processingEnv.getElementUtils().getTypeElement(FastType.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.contains(fastTypeElement)) {
            HashMap<String, Element> nameClassMap = collectFastTypes(roundEnv);
            try {
                buildCreatorClass(nameClassMap);
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
        TypeName typeName = new TypeName("com.exactpro.epfast.annotation.internal.CreatorImpl");
        // mustache files are in UTF-8 by default
        Mustache mustache = mustacheFactory.compile(
            "com/exactpro/epfast/annotation/processing/CreatorImpl.java.mustache");

        JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(typeName.toString());
        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
            mustache.execute(out, new CreatorTemplateParameters(typeName, nameClassMap.entrySet())).flush();
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
