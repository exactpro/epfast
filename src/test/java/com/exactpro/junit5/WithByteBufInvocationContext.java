package com.exactpro.junit5;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;

import java.util.Collection;
import java.util.List;

import static java.util.Collections.singletonList;

public class WithByteBufInvocationContext implements TestTemplateInvocationContext {

    private final List<ByteBuf> arguments;

    private String methodName;

    private WithByteBufMethodContext methodContext;

    WithByteBufInvocationContext(String methodName, WithByteBufMethodContext methodContext, List<ByteBuf> arguments) {
        this.methodName = methodName;
        this.arguments = arguments;
        this.methodContext = methodContext;
    }

    @Override
    public String getDisplayName(int invocationIndex) {
        return formatName(methodName, arguments);
    }

    @Override
    public List<Extension> getAdditionalExtensions() {
        return singletonList(new WithByteBufParameterResolver(methodContext, this.arguments));
    }

    private String formatName(String methodName, Collection<ByteBuf> arguments) {
        StringBuilder displayName = new StringBuilder(methodName);
        for (ByteBuf buf : arguments) {
            appendFormattedBuffer(displayName, buf);
        }
        return displayName.toString();
    }

    private void appendFormattedBuffer(StringBuilder currentName, ByteBuf buf) {
        int readerIndex = buf.readerIndex();
        int readLimit = buf.writerIndex();
        currentName.append(" [ ");
        while (readerIndex < readLimit) {
            appendHexByte(currentName, buf.getByte(readerIndex++));
        }
        currentName.append(']');
    }

    private void appendHexByte(StringBuilder currentName, int aByte) {
        if ((aByte & 0xF0) == 0) {
            currentName.append('0').append(Integer.toHexString(aByte & 0xFF).toUpperCase());
        } else {
            currentName.append(Integer.toHexString(aByte & 0xFF).toUpperCase());
        }
        currentName.append(' ');
    }
}
