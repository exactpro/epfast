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

    private ArrayList<NormalInstruction> readSequenceInstructions(com.exactpro.epfast.template.Sequence sequence) {
        ArrayList<NormalInstruction> normalInstructions = new ArrayList<>();
        normalInstructions.add(new CheckLoop(sequence.getInstructions().size() + 4));
        normalInstructions.add(new SetApplicationType(sequence.getTypeRef()));
        readInstructions(sequence.getInstructions(), normalInstructions);
        normalInstructions.add(new AddToSequence());
        normalInstructions.add(new LoopInstruction());
        normalInstructions.add(new ReadySequence(sequence.getFieldId()));
        return normalInstructions;
    }

    private void readInstructions(List<? extends Instruction> instructions,
                                  ArrayList<NormalInstruction> normalInstructions) {
        for (Instruction instruction : instructions) {
            if (instruction instanceof Group) {
                Group group = (Group) instruction;
                normalInstructions.add(new PushContext());
                normalInstructions.add(new SetInstructions(readGroupInstructions(group)));
            } else if (instruction instanceof com.exactpro.epfast.template.Sequence) {
                com.exactpro.epfast.template.Sequence sequence = (com.exactpro.epfast.template.Sequence) instruction;
                if (sequence.isOptional()) {
                    normalInstructions.add(new SetNullableLengthField(sequence.getLength().getFieldId()));
                } else {
                    normalInstructions.add(new SetMandatoryLengthField(sequence.getLength().getFieldId()));
                }
                normalInstructions.add(new SetSequence());
                normalInstructions.add(new PushContext());
                normalInstructions.add(new SetInstructions(readSequenceInstructions(sequence)));
            } else if (instruction instanceof FieldInstruction) {
                normalInstructions.add(toPrimitiveInstruction((FieldInstruction) instruction));
            } else if (instruction instanceof TemplateRef) {
                TemplateRef templateRef = (TemplateRef) instruction;
                normalInstructions.add(new PushContext());
                normalInstructions.add(new SetInstructionsWithReference(templateRef.getTemplateRef()));
            }
        }
    }

    private NormalInstruction toPrimitiveInstruction(FieldInstruction instruction) {
        if (instruction instanceof Int32Field) {
            if (instruction.isOptional()) {
                return new NullableInt32(instruction.getFieldId());
            } else {
                return new MandatoryInt32(instruction.getFieldId());
            }
        } else if (instruction instanceof AsciiStringField) {
            if (instruction.isOptional()) {
                return new NullableAsciiString(instruction.getFieldId());
            } else {
                return new MandatoryAsciiString(instruction.getFieldId());
            }
        }
        return null;
    }

    public Map<Reference, ArrayList<NormalInstruction>> getInstructionsSet() {
        return instructionsSet;
    }
}
