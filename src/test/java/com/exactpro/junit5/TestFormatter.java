/******************************************************************************
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
 ******************************************************************************/

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



