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
import com.exactpro.epfast.decoder.TemplateNotFoundException;
import com.exactpro.epfast.template.*;
import io.netty.buffer.ByteBuf;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class ExecutionContext {

    // private PresenceMap presenceMap;

    public Map<Reference, ArrayList<NormalInstruction>> instructionsSet;

    public Stack<SavedContext> stack = new Stack<>();

    public ArrayList<NormalInstruction> instructions = new ArrayList<>();

    public int instructionIndex = 0;

    public ByteBuf buffer;

    public ArrayList<IMessage> readyMessages = new ArrayList<>();

    public IMessage applicationMessage;

    public IMessage[] sequence;

    public int sequenceIndex;

    public Integer lengthField;

    public Registers registers = new Registers();

    public String fieldName;

    public ExecutionContext(Map<Reference, ArrayList<NormalInstruction>> instructionsSet) {
        this.instructionsSet = instructionsSet;
    }

    public boolean nextStep() throws OverflowException, TemplateNotFoundException {
        return instructions.get(instructionIndex).execute(this);
    }

    public Collection<IMessage> fetchResults() {
        Collection<IMessage> results = this.readyMessages;
        this.readyMessages = new ArrayList<>();
        return results;
    }

    public static class Registers {

        public int intReg;

        public Integer nullableIntReg;

        public String stringReg;

        public long longReg;

        public Long nullableLongReg;

        public BigInteger bigIntReg;

        public BigDecimal bigDecimalReg;

        public byte[] unicodeReg;

    }

    public static class SavedContext {

        // private final PresenceMap presenceMap;

        private final ArrayList<NormalInstruction> instructions;

        private final int instructionIndex;

        private final IMessage applicationMessage;

        public SavedContext(ExecutionContext ec) {
            this.instructions = ec.instructions;
            this.instructionIndex = ec.instructionIndex;
            this.applicationMessage = ec.applicationMessage;
        }

        public void restoreTo(ExecutionContext ec) {
            ec.instructions = instructions;
            ec.instructionIndex = instructionIndex;
        }

        public void restoreWithGroup(ExecutionContext ec, Reference name) {
            applicationMessage.setField(name.getName(), ec.applicationMessage);
            ec.applicationMessage = applicationMessage;
            ec.instructions = instructions;
            ec.instructionIndex = instructionIndex;
        }

        public void restoreWithSequence(ExecutionContext ec, Reference name) {
            applicationMessage.setField(name.getName(), ec.sequence);
            ec.applicationMessage = applicationMessage;
            ec.instructions = instructions;
            ec.instructionIndex = instructionIndex;
        }
    }
}
