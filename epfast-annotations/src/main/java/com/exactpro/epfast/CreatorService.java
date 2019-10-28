package com.exactpro.epfast;

import java.util.Collection;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

import static com.exactpro.epfast.util.Stream.streamIterable;

public class CreatorService {
    public static Collection<ICreator> providers() {
        return streamIterable(ServiceLoader.load(ICreator.class)).collect(Collectors.toList());
    }
}
