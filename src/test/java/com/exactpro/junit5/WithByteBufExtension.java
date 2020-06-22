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
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;
import org.junit.platform.commons.util.Preconditions;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.platform.commons.util.AnnotationUtils.isAnnotated;

public class WithByteBufExtension implements TestTemplateInvocationContextProvider {

    private static final String METHOD_CONTEXT_KEY = "context";

    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        if (!context.getTestMethod().isPresent()) {
            return false;
        }
        Method testMethod = context.getTestMethod().get();
        if (!isAnnotated(testMethod, WithByteBuf.class)) {
            return false;
        }
        WithByteBufMethodContext methodContext = new WithByteBufMethodContext();
        Preconditions.condition(methodContext.hasPotentiallyValidSignature(testMethod.getParameters()),
            () -> String.format(
                "@WithByteBuf method [%s] declares invalid formal parameter: "
                    + "argument count must be 1 ",
                testMethod.toGenericString()));

        getStore(context).put(METHOD_CONTEXT_KEY, methodContext);
        return true;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(
        ExtensionContext extensionContext) {
        Method templateMethod = extensionContext.getRequiredTestMethod();
        String displayName = extensionContext.getDisplayName();
        WithByteBufMethodContext methodContext = getStore(extensionContext)
            .get(METHOD_CONTEXT_KEY, WithByteBufMethodContext.class);
        String argValue = templateMethod.getAnnotation(WithByteBuf.class).value();
        return generateBufferArguments(argValue)
            .stream()
            .map(arguments -> createInvocationContext(displayName, methodContext, arguments));
    }

    private ArrayList<ArrayList<ByteBuf>> generateBufferArguments(String hexString) {
        ArrayList<ByteBuf> buffers = new ArrayList<>();
        ArrayList<ArrayList<ByteBuf>> argsArray = new ArrayList<>();
        byte[] bytes = ByteBufUtils.bytesFromHex(hexString);

        //add entire array as a first testcase
        buffers.add(ByteBufUtils.buffFromBytes(bytes));
        argsArray.add(buffers);

        //split array in pisces for other testcases
        for (int i = 1; i < bytes.length; i++) {
            buffers = new ArrayList<>();
            buffers.add(ByteBufUtils.buffFromBytes(Arrays.copyOfRange(bytes, 0, i)));
            buffers.add(ByteBufUtils.buffFromBytes(Arrays.copyOfRange(bytes, i, bytes.length)));
            argsArray.add(buffers);
        }
        return argsArray;
    }

    private ExtensionContext.Store getStore(
        ExtensionContext context) {
        return context.getStore(ExtensionContext.Namespace
            .create(WithByteBufExtension.class, context.getRequiredTestMethod()));
    }

    private TestTemplateInvocationContext createInvocationContext(
        String name, WithByteBufMethodContext methodContext, List<ByteBuf> arguments) {
        return new WithByteBufInvocationContext(name, methodContext, arguments);
    }
}
