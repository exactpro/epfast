package com.exactpro.junit5;

import io.netty.buffer.ByteBuf;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestFormatter {

    private String methodName = "aMethod";

    private List<ByteBuf> generateArguments(String[] hexes) {
        List<ByteBuf> arguments = new ArrayList<>();
        for (String hex : hexes) {
            arguments.add(ByteBufUtils.fromHex(hex));
        }
        return arguments;
    }

    @Test
    void testNameOneByte() {
        String[] hexes = {"12"};
        WithByteBufInvocationContext invocationContext =
            new WithByteBufInvocationContext(methodName, new WithByteBufMethodContext(), generateArguments(hexes));
        assertEquals("aMethod [ 12 ]", invocationContext.getDisplayName(1));
    }

    @Test
    void testNameMultipleByteOneBuffer() {
        String[] hexes = {"12 13 A4"};
        WithByteBufInvocationContext invocationContext =
            new WithByteBufInvocationContext(methodName, new WithByteBufMethodContext(), generateArguments(hexes));
        assertEquals("aMethod [ 12 13 A4 ]", invocationContext.getDisplayName(1));
    }

    @Test
    void testNameMultipleByteMultipleBuffer() {
        String[] hexes = {"12 13", "a4", "11"};
        WithByteBufInvocationContext invocationContext =
            new WithByteBufInvocationContext(methodName, new WithByteBufMethodContext(), generateArguments(hexes));
        assertEquals("aMethod [ 12 13 ] [ A4 ] [ 11 ]", invocationContext.getDisplayName(1));
    }
}



