/******************************************************************************
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
 ******************************************************************************/

package com.exactpro.epfast.decoder.message;

import com.exactpro.epfast.decoder.message.instructions.*;
import com.exactpro.epfast.decoder.message.instructions.SetSequence;
import com.exactpro.epfast.template.*;

import java.util.*;

public class InstructionsCompiler {

    Map<Reference, ArrayList<NormalInstruction>> instructionsSet = new HashMap<>();

    private Map<? extends Reference, ? extends Template> templates;

    public InstructionsCompiler(Map<? extends Reference, ? extends Template> templates) {
        this.templates = templates;
    }

    public void compile() {
        for (Map.Entry<? extends Reference, ? extends Template> entry : this.templates.entrySet()) {
            readTemplateInstructions(entry.getValue());
        }
    }

    public void readTemplateInstructions(Template template) {
        ArrayList<NormalInstruction> normalInstructions = new ArrayList<>();
        readInstructions(template.getInstructions(), normalInstructions);
        normalInstructions.add(new PopContext());
        instructionsSet.put(template.getTemplateId(), normalInstructions);
    }

    private ArrayList<NormalInstruction> readGroupInstructions(Group group) {
        ArrayList<NormalInstruction> normalInstructions = new ArrayList<>();
        normalInstructions.add(new SetApplicationType(group.getTypeRef()));
        readInstructions(group.getInstructions(), normalInstructions);
        normalInstructions.add(new ReadyGroup(group.getFieldId()));
        return normalInstructions;
    }

    private ArrayList<NormalInstruction> readSequenceInstructions(Sequence sequence) {
        ArrayList<NormalInstruction> normalInstructions = new ArrayList<>();
        CheckLoop checkLoop = new CheckLoop();
        normalInstructions.add(checkLoop);
        normalInstructions.add(new SetApplicationType(sequence.getTypeRef()));
        readInstructions(sequence.getInstructions(), normalInstructions);
        normalInstructions.add(new AddToSequence());
        normalInstructions.add(new LoopInstruction());
        normalInstructions.add(new ReadySequence(sequence.getFieldId()));
        checkLoop.setJumpIndex(normalInstructions.size() - 1);
        return normalInstructions;
    }

    private void readInstructions(List<? extends Instruction> instructions,
                                  ArrayList<NormalInstruction> normalInstructions) {
        for (Instruction instruction : instructions) {
            if (instruction instanceof Group) {
                Group group = (Group) instruction;
                normalInstructions.add(new PushContext());
                normalInstructions.add(new SetInstructions(readGroupInstructions(group)));
            } else if (instruction instanceof Sequence) {
                Sequence sequence = (Sequence) instruction;
                if (sequence.isOptional()) {
                    normalInstructions.add(new ReadNullableInt32(sequence.getLength().getFieldId()));
                    normalInstructions.add(new SetNullableLengthField());
                } else {
                    normalInstructions.add(new ReadMandatoryInt32(sequence.getLength().getFieldId()));
                    normalInstructions.add(new SetMandatoryLengthField());
                }
                normalInstructions.add(new SetSequence());
                normalInstructions.add(new PushContext());
                normalInstructions.add(new SetInstructions(readSequenceInstructions(sequence)));
            } else if (instruction instanceof FieldInstruction) {
                toPrimitiveInstruction((FieldInstruction) instruction, normalInstructions);
            } else if (instruction instanceof TemplateRef) {
                TemplateRef templateRef = (TemplateRef) instruction;
                normalInstructions.add(new PushContext());
                normalInstructions.add(new SetInstructionsWithReference(templateRef.getTemplateRef()));
            }
        }
    }

    private void toPrimitiveInstruction(FieldInstruction instruction, ArrayList<NormalInstruction> normalInstructions) {
        if (instruction instanceof Int32Field) {
            if (instruction.isOptional()) {
                normalInstructions.add(new ReadNullableInt32(instruction.getFieldId()));
                normalInstructions.add(new SetNullableInt32());
            } else {
                normalInstructions.add(new ReadMandatoryInt32(instruction.getFieldId()));
                normalInstructions.add(new SetMandatoryInt32());
            }
        } else if (instruction instanceof AsciiStringField) {
            if (instruction.isOptional()) {
                normalInstructions.add(new ReadNullableAsciiString(instruction.getFieldId()));
            } else {
                normalInstructions.add(new ReadMandatoryAsciiString(instruction.getFieldId()));
            }
            normalInstructions.add(new SetString());
        }
    }

    public Map<Reference, ArrayList<NormalInstruction>> getInstructionsSet() {
        return instructionsSet;
    }
}
