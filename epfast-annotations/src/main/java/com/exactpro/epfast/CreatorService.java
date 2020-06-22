/*
 * Copyright 2019-2020 Exactpro (Exactpro Systems Limited)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
