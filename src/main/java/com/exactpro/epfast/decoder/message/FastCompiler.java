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
import com.exactpro.epfast.decoder.message.commands.operators.AllOtherOperatorsMissingValue;
import com.exactpro.epfast.decoder.message.commands.operators.AllOtherOperatorsPresentValue;
import com.exactpro.epfast.decoder.message.commands.operators.DefaultMissingValue;
import com.exactpro.epfast.decoder.message.commands.operators.DefaultPresentValue;
import com.exactpro.epfast.decoder.message.commands.presencemap.CheckPresenceBit;
import com.exactpro.epfast.decoder.message.commands.presencemap.ReadPresenceMap;
import com.exactpro.epfast.template.*;

import java.util.*;

public class FastCompiler {

    private final ArrayList<DecoderCommand> commandSet = new ArrayList<>();

    private int presenceBitIndex = 0;

    private FastCompiler() {
    }

    public static Map<Reference, List<DecoderCommand>> compile(Collection<? extends Template> templates) {
        HashMap<Reference, List<DecoderCommand>> commandSets = new HashMap<>();
        for (Template template : templates) {
            commandSets.put(template.getTemplateId(),
                compileSubroutine(template.getTypeRef(), template.getInstructions()));
        }
        return commandSets;
    }

    private static ArrayList<DecoderCommand> compileSubroutine(
        Reference typeRef, Collection<? extends Instruction> instructions) {
        FastCompiler compiler = new FastCompiler();
        compiler.compile(typeRef, instructions);
        return compiler.commandSet;
    }

    private void compile(
        Reference typeRef, Collection<? extends Instruction> instructions) {
        if (typeRef != null) {
            commandSet.add(new InitApplicationType(typeRef));
        }
        if (requiresPresenceMap(instructions)) {
            commandSet.add(new ReadPresenceMap());
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
        if (group.isOptional()) {
            commandSet.add(new CheckPresenceBit(presenceBitIndex));
            presenceBitIndex++;
        }
        commandSet.add(new StaticCall(compileSubroutine(group.getTypeRef(), group.getInstructions())));
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
        commandSet.add(new StaticCall(compileSubroutine(sequence.getTypeRef(), sequence.getInstructions())));
        commandSet.add(new SetIndexedApplicationTypeProperty(sequence.getFieldId()));
        commandSet.add(new EndLoop(loopCommandIndex - commandSet.size()));
        loop.setJumpOffset(commandSet.size() - loopCommandIndex);
    }

    private void compileFieldInstruction(FieldInstruction instruction) {
        if (instruction instanceof Int32Field) {
            FieldOperator operator = ((Int32Field) instruction).getOperator();
            if (requiresBit(operator)) {
                commandSet.add(new CheckPresenceBit(presenceBitIndex));
                presenceBitIndex++;
            }
            if (instruction.isOptional()) {
                commandSet.add(new ReadNullableInt32());
                addOperator(operator);
                commandSet.add(new SetNullableInt32(instruction.getFieldId()));
            } else {
                commandSet.add(new ReadMandatoryInt32());
                addOperator(operator);
                commandSet.add(new SetMandatoryInt32(instruction.getFieldId()));
            }
        } else if (instruction instanceof AsciiStringField) {
            FieldOperator operator = ((AsciiStringField) instruction).getOperator();

            if (requiresBit(operator)) {
                commandSet.add(new CheckPresenceBit(presenceBitIndex));
                presenceBitIndex++;
            }
            if (instruction.isOptional()) {
                commandSet.add(new ReadNullableAsciiString());
            } else {
                commandSet.add(new ReadMandatoryAsciiString());
            }
            addOperator(operator);
            commandSet.add(new SetString(instruction.getFieldId()));
        }
    }

    private boolean requiresPresenceMap(Collection<? extends Instruction> instructions) {
        for (Instruction instruction : instructions) {
            if (instruction instanceof Int32Field) {
                if (requiresBit(((Int32Field) instruction).getOperator())) {
                    return true;
                }
            } else if (instruction instanceof AsciiStringField) {
                if (requiresBit(((AsciiStringField) instruction).getOperator())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean requiresBit(FieldOperator operator) {
        return operator instanceof CopyOperator
            || operator instanceof DefaultOperator
            || operator instanceof IncrementOperator
            || operator instanceof TailOperator;
    }

    private void addOperator(FieldOperator operator) {
        int offset = 2;
        if (operator instanceof DefaultOperator) {
            commandSet.add(new DefaultPresentValue(offset));
            commandSet.add(new DefaultMissingValue());
        } else {
            commandSet.add(new AllOtherOperatorsPresentValue(offset));
            commandSet.add(new AllOtherOperatorsMissingValue());
        }
    }
}
