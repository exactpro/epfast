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
import com.exactpro.epfast.decoder.OverflowException;
import com.exactpro.epfast.template.*;
import io.netty.buffer.ByteBuf;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class ExecutionContext {

    // private PresenceMap presenceMap;

    public Map<Reference, ArrayList<NormalInstruction>> compiledTemplates;

    public Stack<SavedContext> callStack = new Stack<>();

    public ArrayList<NormalInstruction> instructions = new ArrayList<>();

    public int nextInstructionIndex = 0;

    public ByteBuf buffer;

    public ArrayList<IMessage> readyMessages = new ArrayList<>();

    public IMessage applicationMessage;

    public IMessage[] sequence;

    public int loopIndex;

    public int loopLimit;

    public Registers registers = new Registers();

    public ExecutionContext(Map<Reference, ArrayList<NormalInstruction>> compiledTemplates,
                            ArrayList<NormalInstruction> bootstrapCode) {
        this.compiledTemplates = compiledTemplates;
    }

    public boolean nextStep() throws OverflowException {
        return instructions.get(nextInstructionIndex).execute(this);
    }

    public void call() {
        nextInstructionIndex++;
        callStack.push(new ExecutionContext.SavedContext(this));
    }

    public void ret() {
        callStack.pop().restoreTo(this);
    }

    public Collection<IMessage> fetchResults() {
        Collection<IMessage> results = this.readyMessages;
        this.readyMessages = new ArrayList<>();
        return results;
    }

    public static class Registers {

        public int mandatoryInt32Value;

        public Integer optionalInt32Value;

        public String stringValue;

        public long mandatoryInt64Value;

        public Long optionalInt64Value;

        public BigInteger unsignedInt64Value;

        public BigDecimal decimalValue;

        public IMessage group;
    }

    public static class SavedContext {

        // private final PresenceMap presenceMap;

        private final ArrayList<NormalInstruction> instructions;

        private final int instructionIndex;

        private final int loopLimit;

        private final int loopIndex;

        private final IMessage applicationMessage;

        public SavedContext(ExecutionContext ec) {
            this.instructions = ec.instructions;
            this.instructionIndex = ec.nextInstructionIndex;
            this.loopLimit = ec.loopLimit;
            this.loopIndex = ec.loopIndex;
            this.applicationMessage = ec.applicationMessage;
        }

        public void restoreTo(ExecutionContext ec) {
            ec.instructions = instructions;
            ec.loopLimit = loopLimit;
            ec.loopIndex = loopIndex;
            ec.nextInstructionIndex = instructionIndex;
            ec.applicationMessage = applicationMessage;
        }
    }
}
