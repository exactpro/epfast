package com.exactpro.epfast;

import java.util.Collection;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

import static com.exactpro.epfast.util.Stream.streamIterable;

public class CreatorService {
    private static Collection<ICreator> providers;

    private static Collection<ICreator> getProviders() {
        return streamIterable(ServiceLoader.load(ICreator.class)).collect(Collectors.toList());
    }

    public static ICreator getCreator(String fastPackage) {
        if (providers == null) {
            synchronized (CreatorService.class) {
                if (providers == null) {
                    providers = getProviders();
                }
            }
        }
        return providers.stream().filter(iCreator -> iCreator.getFastPackage().equals(fastPackage))
            .findFirst().orElseThrow(() ->
                new RuntimeException(String.format("Creator with FastPackage=\"%s\" not found", fastPackage)));
    }
}
