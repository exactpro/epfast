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

package com.exactpro.epfast.decoder.message;

import com.exactpro.epfast.decoder.IMessage;
import com.exactpro.epfast.decoder.presencemap.PresenceMap;
import com.exactpro.epfast.template.*;
import io.netty.buffer.ByteBuf;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class DecoderState {

    public boolean canProceed = true;

    public ByteBuf inputBuffer; // input

    public ArrayList<IMessage> decodedMessages = new ArrayList<>(); // output

    public PresenceMap presenceMap;

    public final Map<Reference, List<DecoderCommand>> commandSets;

    public Stack<CallStackFrame> callStack = new Stack<>();

    public List<DecoderCommand> activeCommandSet;

    public int nextCommandIndex = 0;

    public int nextPresenceBitIndex = 0;

    public IMessage activeMessage;

    public int loopIndex;

    public long loopLimit;

    public final UnionRegister register = new UnionRegister();

    public DecoderState(Map<Reference, List<DecoderCommand>> commandSets,
                        List<DecoderCommand> bootstrapCommands) {
        this.commandSets = Objects.requireNonNull(commandSets, "commandSets cannot be null");
        this.activeCommandSet = Objects.requireNonNull(bootstrapCommands, "bootstrapCommands cannot be null");
    }

    public DecoderCommand nextCommand() {
        return activeCommandSet.get(nextCommandIndex);
    }

    public void call(List<DecoderCommand> commandSet) {
        nextCommandIndex++;
        callStack.push(new CallStackFrame(this));
        activeCommandSet = commandSet;
        nextCommandIndex = 0;
    }

    public void ret() {
        callStack.pop().restoreTo(this);
    }

    public Collection<?> readMessages() {
        ArrayList<IMessage> messages = decodedMessages;
        decodedMessages = new ArrayList<>();
        return messages;
    }

    // Logically it's one register. It's up to application to ensure that the right field is used for read / write.
    public static class UnionRegister {

        // public boolean isNull;

        public int mandatoryInt32Value;

        public Integer optionalInt32Value;

        public Long optionalUInt32Value;

        public long mandatoryUInt32Value;

        public String stringValue;

        public long mandatoryInt64Value;

        public Long optionalInt64Value;

        public BigInteger unsignedInt64Value;

        public BigDecimal decimalValue;

        public Object applicationValue;
    }

    public static class CallStackFrame {

        // private final PresenceMap presenceMap;

        private final List<DecoderCommand> activeCommandSet;

        private final int nextCommandIndex;

        private final long loopLimit;

        private final int loopIndex;

        private final IMessage activeMessage;

        public CallStackFrame(DecoderState decoderState) {
            this.activeCommandSet = decoderState.activeCommandSet;
            this.nextCommandIndex = decoderState.nextCommandIndex;
            this.loopLimit = decoderState.loopLimit;
            this.loopIndex = decoderState.loopIndex;
            this.activeMessage = decoderState.activeMessage;
        }

        public void restoreTo(DecoderState decoderState) {
            decoderState.activeCommandSet = activeCommandSet;
            decoderState.loopLimit = loopLimit;
            decoderState.loopIndex = loopIndex;
            decoderState.nextCommandIndex = nextCommandIndex;
            decoderState.activeMessage = activeMessage;
        }
    }
}
