/*
 * Copyright 2020 Exactpro (Exactpro Systems Limited)
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

package com.exactpro.epfast.decoder.message;

import com.exactpro.epfast.decoder.message.commands.*;
import com.exactpro.epfast.decoder.message.commands.InitIndexedProperty;
import com.exactpro.epfast.decoder.message.commands.ascii.ReadMandatoryAsciiString;
import com.exactpro.epfast.decoder.message.commands.ascii.ReadNullableAsciiString;
import com.exactpro.epfast.decoder.message.commands.ascii.SetString;
import com.exactpro.epfast.decoder.message.commands.integer.*;
import com.exactpro.epfast.template.*;

import java.util.*;

public class FastCompiler {

    private final ArrayList<DecoderCommand> commandSet = new ArrayList<>();

    private boolean isMapPresent = false;

    private FastCompiler() {
    }

    public static Map<Reference, List<DecoderCommand>> compile(Collection<? extends Template> templates) {
        HashMap<Reference, List<DecoderCommand>> commandSets = new HashMap<>();
        for (Template template : templates) {
            FastCompiler compiler = compileSubroutine(template.getTypeRef(), template.getInstructions());
            commandSets.put(template.getTemplateId(), compiler.commandSet);
        }
        return commandSets;
    }

    private static FastCompiler compileSubroutine(
        Reference typeRef, Collection<? extends Instruction> instructions) {
        FastCompiler compiler = new FastCompiler();
        compiler.compile(typeRef, instructions);
        return compiler;
    }

    private void compile(
        Reference typeRef, Collection<? extends Instruction> instructions) {
        isMapPresent = false;
        if (typeRef != null) {
            commandSet.add(new InitApplicationType(typeRef));
        }
        for (Instruction instruction : instructions) {
            if (instruction instanceof Group) {
                compileGroup((Group) instruction);
            } else if (instruction instanceof Sequence) {
                compileSequence((Sequence) instruction);
            } else if (instruction instanceof FieldInstruction) {
                compileFieldInstruction((FieldInstruction) instruction);
            } else if (instruction instanceof TemplateRef) {
                TemplateRef templateRef = (TemplateRef) instruction;
                // TODO dereference
                commandSet.add(new CallByReference(templateRef.getTemplateRef()));
            }
        }
        commandSet.add(new RetApplicationType());
    }

    private void compileGroup(Group group) {
        FastCompiler compiler = compileSubroutine(group.getTypeRef(), group.getInstructions());
        if (group.isOptional()) {
            //commandSet.add(new CheckPresenceBit());
        }
        if (compiler.isMapPresent) {
            //commandSet.add(new ReadPresenceMap());
        }
        commandSet.add(new StaticCall(compiler.commandSet));
        commandSet.add(new SetApplicationTypeProperty(group.getFieldId()));
    }

    private void compileSequence(Sequence sequence) {
        if (sequence.isOptional()) {
            commandSet.add(new ReadNullableUInt32());
            commandSet.add(new SetNullableLengthField());
        } else {
            commandSet.add(new ReadMandatoryUInt32());
            commandSet.add(new SetMandatoryLengthField());
        }
        commandSet.add(new InitIndexedProperty(sequence.getFieldId()));
        int loopCommandIndex = commandSet.size();
        BeginLoop loop = new BeginLoop();
        commandSet.add(loop);
        FastCompiler compiler = compileSubroutine(sequence.getTypeRef(), sequence.getInstructions());
        if (compiler.isMapPresent) {
            //commandSet.add(new ReadPresenceMap());
        }
        commandSet.add(new StaticCall(compiler.commandSet));
        commandSet.add(new SetIndexedApplicationTypeProperty(sequence.getFieldId()));
        commandSet.add(new EndLoop(loopCommandIndex));
        loop.setJumpIndex(commandSet.size());
    }

    private void compileFieldInstruction(FieldInstruction instruction) {
        if (instruction instanceof Int32Field) {
            checkIntegerFieldOperators(((Int32Field) instruction).getOperator());
            if (instruction.isOptional()) {
                commandSet.add(new ReadNullableInt32());
                commandSet.add(new SetNullableInt32(instruction.getFieldId()));
            } else {
                commandSet.add(new ReadMandatoryInt32());
                commandSet.add(new SetMandatoryInt32(instruction.getFieldId()));
            }
        } else if (instruction instanceof AsciiStringField) {
            checkVectorFieldOperators(((AsciiStringField) instruction).getOperator());
            if (instruction.isOptional()) {
                commandSet.add(new ReadNullableAsciiString());
            } else {
                commandSet.add(new ReadMandatoryAsciiString());
            }
            commandSet.add(new SetString(instruction.getFieldId()));
        }
    }

    private void checkIntegerFieldOperators(FieldOperator operator) {
        if (operator instanceof CopyOperator
            || operator instanceof DefaultOperator
            || operator instanceof IncrementOperator) {
            //commandSet.add(new CheckPresenceBit());
            isMapPresent = true;
        }
    }

    private void checkVectorFieldOperators(FieldOperator operator) {
        if (operator instanceof CopyOperator
            || operator instanceof DefaultOperator
            || operator instanceof IncrementOperator
            || operator instanceof TailOperator) {
            //commandSet.add(new CheckPresenceBit());
            isMapPresent = true;
        }
    }
}
