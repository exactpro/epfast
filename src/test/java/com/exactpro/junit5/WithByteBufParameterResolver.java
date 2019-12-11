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
