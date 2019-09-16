package com.exactpro.epfast.annotation.processing;

import com.exactpro.epfast.FastType;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SupportedAnnotationTypes("com.exactpro.epfast.FastType")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class FastProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        HashMap<String, Element> nameClassMap = processRound(annotations, roundEnv);

        if (!nameClassMap.isEmpty()) {
            try {
                buildCreatorClass(nameClassMap);
            } catch (Exception e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
            }
        }
        return !annotations.isEmpty();
    }

    private HashMap<String, Element> filterDuplicates(HashMap<String, HashSet<Element>> nameClassMap) {
        HashMap<String, Element> finalNameClassMap = new HashMap<>();
        for (Map.Entry<String, HashSet<Element>> entry : nameClassMap.entrySet()) {
            String fastTypeName = entry.getKey();
            HashSet<Element> elements = entry.getValue();
            if (elements.size() > 1) {
                reportDuplicate(fastTypeName, elements);
            } else {
                finalNameClassMap.put(fastTypeName, elements.iterator().next());
            }
        }
        return finalNameClassMap;
    }

    private HashMap<String, Element> processRound(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        HashMap<String, HashSet<Element>> nameClassMap = new HashMap<>();
        Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(FastType.class);
        for (Element element : annotatedElements) {
            String fastTypeName = element.getAnnotation(FastType.class).name();
            if (fastTypeName.isEmpty()) {
                fastTypeName = element.getSimpleName().toString();
            }
            putElement(nameClassMap, fastTypeName, element);
        }
        return filterDuplicates(nameClassMap);
    }

    private void putElement(HashMap<String, HashSet<Element>> nameClassMap, String fastTypeName, Element element) {
        HashSet<Element> currentMappedElements = nameClassMap.get(fastTypeName);
        if (currentMappedElements == null) {
            currentMappedElements = new HashSet<>();
        }
        currentMappedElements.add(element);
        nameClassMap.put(fastTypeName, currentMappedElements);
    }

    private void reportDuplicate(String fastTypeName, HashSet<Element> elements) {
        elements.forEach(element -> processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Processing for " + element + " is skipped!! Multiple classes have FastType annotation name = " + fastTypeName, element));
    }

    private void buildCreatorClass(HashMap<String, Element> nameClassMap) throws Exception {
        String className = "CreatorImpl";
        JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(className);
        PrintWriter out = new PrintWriter(builderFile.openWriter());
        out.println("package com.exactpro.epfast.annotation;");
        out.println("import java.util.HashMap;");
        out.println("import java.util.Map;");
        out.println("public class " + className + " implements ICreator{");
        out.println("private HashMap<String, Class> nameClassMap = new HashMap<>();");
        out.println("public " + className + "() throws Exception{");
        nameClassMap.forEach((key, value) -> {
            out.print("nameClassMap.put(\"" + key + "\",");
            out.print(value + ".class");
            out.println(");");
        });
        out.print("}");
        out.println("public Object create(String name) throws Exception {");
        out.println("if  (nameClassMap.containsKey(name)) return nameClassMap.get(name).newInstance();" +
                "throw new RuntimeException(\"FastType name=\"+name+\" not found\");");
        out.print("}");
        out.print("}");
        out.close();
    }

}
