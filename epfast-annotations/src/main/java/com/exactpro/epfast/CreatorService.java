package com.exactpro.epfast;

import java.util.Collection;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

import static com.exactpro.epfast.util.Stream.streamIterable;

public class CreatorService {

    public static ICreator getCreator(String fastPackage) {
        return Lazy.providers.stream()
            .filter(iCreator -> iCreator.getFastPackage().equals(fastPackage))
            .findFirst().orElseThrow(() ->
                new RuntimeException(String.format("Creator with FastPackage=\"%s\" not found", fastPackage)));
    }

    private static class Lazy {
        private static final Collection<ICreator> providers = getProviders();

        private static Collection<ICreator> getProviders() {
            return streamIterable(ServiceLoader.load(ICreator.class)).collect(Collectors.toList());
        }
    }
}
