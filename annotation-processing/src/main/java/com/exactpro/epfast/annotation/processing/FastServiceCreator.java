package com.exactpro.epfast.annotation.processing;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.*;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class FastServiceCreator {
    private Filer filer;

    public FastServiceCreator(ProcessingEnvironment processingEnvironment) {
        this.filer = processingEnvironment.getFiler();
    }

    public void updateServiceEntries(TypeName service, Set<TypeName> providers,
                                     Element... originatingElements) throws IOException {
        Set<TypeName> allProviders = getExistingServiceProviders(service);
        if (allProviders.containsAll(providers)) {
            return;
        }
        allProviders.addAll(providers);
        FileObject resourceFile = filer.createResource(
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

    private static String getServicesFilePath(TypeName service) {
        return Paths.get("META-INF", "services", service.toString()).toString();
    }

    private Set<TypeName> getExistingServiceProviders(TypeName service) throws IOException {
        FileObject existingFile = filer.getResource(
            StandardLocation.CLASS_OUTPUT,
            "",
            getServicesFilePath(service));

        try (BufferedReader br = new BufferedReader(new InputStreamReader(existingFile.openInputStream()))) {
            return br.lines().filter(provider -> !provider.isEmpty())
                .map(TypeName::new).collect(Collectors.toSet());
        } catch (FileNotFoundException e) {
            // Resource didn't exist before, so we ignore it
        }
        return new HashSet<>();
    }
}
