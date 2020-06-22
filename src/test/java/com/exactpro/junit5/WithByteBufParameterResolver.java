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

package com.exactpro.junit5;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.List;

public class WithByteBufParameterResolver implements ParameterResolver {

    private final WithByteBufMethodContext methodContext;

    private final List<ByteBuf> arguments;

    WithByteBufParameterResolver(WithByteBufMethodContext methodContext, List<ByteBuf> arguments) {
        this.methodContext = methodContext;
        this.arguments = arguments;
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        Executable declaringExecutable = parameterContext.getDeclaringExecutable();
        Method testMethod = extensionContext.getTestMethod().orElse(null);
        return declaringExecutable.equals(testMethod);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
        throws ParameterResolutionException {

        return this.methodContext.resolve(parameterContext, this.arguments);
    }
}
