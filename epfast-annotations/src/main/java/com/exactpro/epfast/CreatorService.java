package com.exactpro.epfast;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class CreatorService {
    public static List<ICreator> providers() {
        List<ICreator> services = new ArrayList<>();
        ServiceLoader<ICreator> loader = ServiceLoader.load(ICreator.class);
        loader.forEach(services::add);
        return services;
    }

}
