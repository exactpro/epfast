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

import java.lang.reflect.Parameter;
import java.util.List;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.params.converter.ArgumentConverter;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.DefaultArgumentConverter;
import org.junit.jupiter.params.support.AnnotationConsumerInitializer;
import org.junit.platform.commons.util.AnnotationUtils;
import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.platform.commons.util.StringUtils;

class WithByteBufMethodContext {

    boolean hasPotentiallyValidSignature(Parameter[] parameters) {
        return parameters.length == 1;
    }

    Object resolve(ParameterContext parameterContext, List<ByteBuf> arguments) {
        return createResolver(parameterContext).resolve(parameterContext, arguments);
    }

    private WithByteBufMethodContext.Resolver createResolver(ParameterContext parameterContext) {
        try { // @formatter:off
            return AnnotationUtils.findAnnotation(parameterContext.getParameter(), ConvertWith.class)
                .map(ConvertWith::value)
                .map(clazz -> (ArgumentConverter) ReflectionUtils.newInstance(clazz))
                .map(converter -> AnnotationConsumerInitializer.initialize(
                    parameterContext.getParameter(), converter)
                )
                .map(WithByteBufMethodContext.Converter::new)
                .orElse(WithByteBufMethodContext.Converter.DEFAULT);
        } catch (Exception ex) {
            throw parameterResolutionException("Error creating ArgumentConverter", ex, parameterContext);
        }
    }

    interface Resolver {

        Object resolve(ParameterContext parameterContext, List<ByteBuf> arguments);

    }

    static class Converter implements WithByteBufMethodContext.Resolver {

        private static final WithByteBufMethodContext.Converter DEFAULT =
            new WithByteBufMethodContext.Converter(DefaultArgumentConverter.INSTANCE);

        private final ArgumentConverter argumentConverter;

        Converter(ArgumentConverter argumentConverter) {
            this.argumentConverter = argumentConverter;
        }

        @Override
        public Object resolve(ParameterContext parameterContext, List<ByteBuf> argument) {
            try {
                return this.argumentConverter.convert(argument, parameterContext);
            } catch (Exception ex) {
                throw parameterResolutionException("Error converting parameter", ex, parameterContext);
            }
        }

    }

    private static ParameterResolutionException parameterResolutionException(String message, Exception cause,
                                                                             ParameterContext parameterContext) {
        String fullMessage = message + " at index " + parameterContext.getIndex();
        if (StringUtils.isNotBlank(cause.getMessage())) {
            fullMessage += ": " + cause.getMessage();
        }
        return new ParameterResolutionException(fullMessage, cause);
    }
}
