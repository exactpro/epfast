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

package com.exactpro.epfast.template.assertion;

import com.exactpro.epfast.template.*;

import java.util.List;

import static com.exactpro.epfast.template.assertion.IdentitiesEquality.*;
import static com.exactpro.epfast.template.assertion.OperatorsEquality.areEqualOperators;

public class InstructionsEquality {

    static boolean areEqualInstructionLists(List<? extends Instruction> actual, List<? extends Instruction> expected) {
        if (actual == expected) {
            return true;
        }
        if (actual.size() != expected.size()) {
            return false;
        }

        for (int i = 0; i < actual.size(); i++) {

            Instruction actualItem = actual.get(i);
            Instruction expectedItem = expected.get(i);
            if (actualItem != expectedItem) {

                if ((actualItem instanceof TemplateRef) && (expectedItem instanceof TemplateRef)) {
                    if (!areEqual((TemplateRef) actualItem, (TemplateRef) expectedItem)) {
                        return false;
                    }
                } else if ((actualItem instanceof Int32Field) && (expectedItem instanceof Int32Field)) {
                    if (!areEqual((Int32Field) actualItem, (Int32Field) expectedItem)) {
                        return false;
                    }
                } else if ((actualItem instanceof Int64Field) && (expectedItem instanceof Int64Field)) {
                    if (!areEqual((Int64Field) actualItem, (Int64Field) expectedItem)) {
                        return false;
                    }
                } else if ((actualItem instanceof UInt32Field) && (expectedItem instanceof UInt32Field)) {
                    if (!areEqual((UInt32Field) actualItem, (UInt32Field) expectedItem)) {
                        return false;
                    }
                } else if ((actualItem instanceof UInt64Field) && (expectedItem instanceof UInt64Field)) {
                    if (!areEqual((UInt64Field) actualItem, (UInt64Field) expectedItem)) {
                        return false;
                    }
                } else if ((actualItem instanceof AsciiStringField) && (expectedItem instanceof AsciiStringField)) {
                    if (!areEqual((AsciiStringField) actualItem, (AsciiStringField) expectedItem)) {
                        return false;
                    }
                } else if ((actualItem instanceof UnicodeStringField) && (expectedItem instanceof UnicodeStringField)) {
                    if (!areEqual((UnicodeStringField) actualItem, (UnicodeStringField) expectedItem)) {
                        return false;
                    }
                } else if ((actualItem instanceof ByteVectorField) && (expectedItem instanceof ByteVectorField)) {
                    if (!areEqual((ByteVectorField) actualItem, (ByteVectorField) expectedItem)) {
                        return false;
                    }
                } else if ((actualItem instanceof CompoundDecimalField) &&
                    (expectedItem instanceof CompoundDecimalField)) {
                    if (!areEqual((CompoundDecimalField) actualItem, (CompoundDecimalField) expectedItem)) {
                        return false;
                    }
                } else if ((actualItem instanceof SimpleDecimalField) && (expectedItem instanceof SimpleDecimalField)) {
                    if (!areEqual((SimpleDecimalField) actualItem, (SimpleDecimalField) expectedItem)) {
                        return false;
                    }
                } else if ((actualItem instanceof Group) && (expectedItem instanceof Group)) {
                    if (!areEqual((Group) actualItem, (Group) expectedItem)) {
                        return false;
                    }
                } else if ((actualItem instanceof Sequence) && (expectedItem instanceof Sequence)) {
                    if (!areEqual((Sequence) actualItem, (Sequence) expectedItem)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean areEqual(TemplateRef actual, TemplateRef expected) {
        return areEqualReferences(actual.getTemplateRef(), expected.getTemplateRef());
    }

    private static boolean areEqual(Int32Field actual, Int32Field expected) {
        return areEqualFieldInstructions(actual, expected) &&
            areEqualOperators(actual.getOperator(), expected.getOperator());
    }

    private static boolean areEqual(Int64Field actual, Int64Field expected) {
        return areEqualFieldInstructions(actual, expected) &&
            areEqualOperators(actual.getOperator(), expected.getOperator());
    }

    private static boolean areEqual(UInt32Field actual, UInt32Field expected) {
        return areEqualFieldInstructions(actual, expected) &&
            areEqualOperators(actual.getOperator(), expected.getOperator());
    }

    private static boolean areEqual(UInt64Field actual, UInt64Field expected) {
        return areEqualFieldInstructions(actual, expected) &&
            areEqualOperators(actual.getOperator(), expected.getOperator());
    }

    private static boolean areEqual(AsciiStringField actual, AsciiStringField expected) {
        return areEqualFieldInstructions(actual, expected) &&
            areEqualOperators(actual.getOperator(), expected.getOperator());
    }

    private static boolean areEqual(UnicodeStringField actual, UnicodeStringField expected) {
        return areEqualFieldInstructions(actual, expected) &&
            areEqualOperators(actual.getOperator(), expected.getOperator()) &&
            areEqualIdentities(actual.getLengthFieldId(), expected.getLengthFieldId());
    }

    private static boolean areEqual(ByteVectorField actual, ByteVectorField expected) {
        return areEqualFieldInstructions(actual, expected) &&
            areEqualOperators(actual.getOperator(), expected.getOperator()) &&
            areEqualIdentities(actual.getLengthFieldId(), expected.getLengthFieldId());
    }

    private static boolean areEqual(CompoundDecimalField actual, CompoundDecimalField expected) {
        return areEqualFieldInstructions(actual, expected) &&
            areEqualOperators(actual.getExponent(), expected.getExponent()) &&
            areEqualOperators(actual.getMantissa(), expected.getMantissa());
    }

    private static boolean areEqual(SimpleDecimalField actual, SimpleDecimalField expected) {
        return areEqualFieldInstructions(actual, expected) &&
            areEqualOperators(actual.getOperator(), expected.getOperator());
    }

    private static boolean areEqual(Group actual, Group expected) {
        return areEqualFieldInstructions(actual, expected) &&
            areEqualReferences(actual.getTypeRef(), expected.getTypeRef()) &&
            areEqualInstructionLists(actual.getInstructions(), expected.getInstructions());
    }

    private static boolean areEqual(Sequence actual, Sequence expected) {
        return areEqualFieldInstructions(actual, expected) &&
            areEqualReferences(actual.getTypeRef(), expected.getTypeRef()) &&
            areEqualInstructionLists(actual.getInstructions(), expected.getInstructions()) &&
            (actual.getLength() == expected.getLength() ||
                (areEqualIdentities(actual.getLength().getFieldId(), expected.getLength().getFieldId()) &&
                    areEqualOperators(actual.getLength().getOperator(), expected.getLength().getOperator())));
    }

    private static boolean areEqualFieldInstructions(FieldInstruction actual, FieldInstruction expected) {
        return areEqualIdentities(actual.getFieldId(), expected.getFieldId()) &&
            actual.isOptional() == expected.isOptional();
    }
}
