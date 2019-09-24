package com.exactpro.epfast.annotation.processing;

import com.exactpro.epfast.FastType;
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
@SupportedAnnotationTypes("com.exactpro.epfast.FastType")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class FastProcessor extends AbstractProcessor {

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
        String packageName = "com.exactpro.epfast.annotation.internal";
        String className = "CreatorImpl";
        JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(packageName + "." + className);
        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
            out.println("package " + packageName + ";");
            out.println("import java.util.HashMap;");
            out.println("import java.util.Map;");
            out.println("import com.exactpro.epfast.annotation.ICreator;");
            out.println("public class " + className + " implements ICreator {");
            out.println("public Object create(String name) {");
            nameClassMap.forEach((key, value) -> {
                out.println("if (\"" + key + "\".equals(name)) {");
                out.println("return new " + value + "();");
                out.print("} else ");
            });
            out.println("{");
            out.print("throw new RuntimeException(\"FastType name=\"+name+\" not found.\");");
            out.print("}");
            out.print("}");
            out.print("}");
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
