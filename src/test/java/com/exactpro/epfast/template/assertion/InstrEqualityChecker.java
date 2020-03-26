package com.exactpro.epfast.template.assertion;

import com.exactpro.epfast.template.*;

import java.util.List;

import static com.exactpro.epfast.template.assertion.OpEqualityChecker.areSameOperators;
import static com.exactpro.epfast.template.assertion.TemplateAssert.areSameIdentities;
import static com.exactpro.epfast.template.assertion.TemplateAssert.areSameReferences;

public class InstrEqualityChecker {

    static boolean areSameInstructions(List<? extends Instruction> actual, List<? extends Instruction> instr) {
        if (actual == instr) {
            return true;
        }
        if (actual.size() != instr.size()) {
            return false;
        }

        for (int i = 0; i < actual.size(); i++) {

            Instruction actualItem = actual.get(i);
            Instruction instrItem = instr.get(i);
            if (actualItem != instrItem) {
                if (!actualItem.getClass().equals(instrItem.getClass())) {
                    return false;
                }

                if (actualItem instanceof TemplateRef) {
                    if (!areEqual((TemplateRef) actualItem, (TemplateRef) instrItem)) {
                        return false;
                    }
                } else if (actualItem instanceof Int32Field) {
                    if (!areEqual((Int32Field) actualItem, (Int32Field) instrItem)) {
                        return false;
                    }
                } else if (actualItem instanceof Int64Field) {
                    if (!areEqual((Int64Field) actualItem, (Int64Field) instrItem)) {
                        return false;
                    }
                } else if (actualItem instanceof UInt32Field) {
                    if (!areEqual((UInt32Field) actualItem, (UInt32Field) instrItem)) {
                        return false;
                    }
                } else if (actualItem instanceof UInt64Field) {
                    if (!areEqual((UInt64Field) actualItem, (UInt64Field) instrItem)) {
                        return false;
                    }
                } else if (actualItem instanceof AsciiStringField) {
                    if (!areEqual((AsciiStringField) actualItem, (AsciiStringField) instrItem)) {
                        return false;
                    }
                } else if (actualItem instanceof UnicodeStringField) {
                    if (!areEqual((UnicodeStringField) actualItem, (UnicodeStringField) instrItem)) {
                        return false;
                    }
                } else if (actualItem instanceof ByteVectorField) {
                    if (!areEqual((ByteVectorField) actualItem, (ByteVectorField) instrItem)) {
                        return false;
                    }
                } else if (actualItem instanceof CompoundDecimalField) {
                    if (!areEqual((CompoundDecimalField) actualItem, (CompoundDecimalField) instrItem)) {
                        return false;
                    }
                } else if (actualItem instanceof SimpleDecimalField) {
                    if (!areEqual((SimpleDecimalField) actualItem, (SimpleDecimalField) instrItem)) {
                        return false;
                    }
                } else if (actualItem instanceof Group) {
                    if (!areEqual((Group) actualItem, (Group) instrItem)) {
                        return false;
                    }
                } else if (actualItem instanceof Sequence) {
                    if (!areEqual((Sequence) actualItem, (Sequence) instrItem)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean areEqual(TemplateRef actual, TemplateRef instr) {
        return areSameReferences(actual.getTemplateRef(), instr.getTemplateRef());
    }

    private static boolean areEqual(Int32Field actual, Int32Field instr) {
        return areSameFieldInstructions(actual, instr) && areSameOperators(actual.getOperator(), instr.getOperator());
    }

    private static boolean areEqual(Int64Field actual, Int64Field instr) {
        return areSameFieldInstructions(actual, instr) && areSameOperators(actual.getOperator(), instr.getOperator());
    }

    private static boolean areEqual(UInt32Field actual, UInt32Field instr) {
        return areSameFieldInstructions(actual, instr) && areSameOperators(actual.getOperator(), instr.getOperator());
    }

    private static boolean areEqual(UInt64Field actual, UInt64Field instr) {
        return areSameFieldInstructions(actual, instr) && areSameOperators(actual.getOperator(), instr.getOperator());
    }

    private static boolean areEqual(AsciiStringField actual, AsciiStringField instr) {
        return areSameFieldInstructions(actual, instr) && areSameOperators(actual.getOperator(), instr.getOperator());
    }

    private static boolean areEqual(UnicodeStringField actual, UnicodeStringField instr) {
        return areSameFieldInstructions(actual, instr) && areSameOperators(actual.getOperator(), instr.getOperator()) &&
            areSameIdentities(actual.getLengthFieldId(), instr.getLengthFieldId());
    }

    private static boolean areEqual(ByteVectorField actual, ByteVectorField instr) {
        return areSameFieldInstructions(actual, instr) && areSameOperators(actual.getOperator(), instr.getOperator()) &&
            areSameIdentities(actual.getLengthFieldId(), instr.getLengthFieldId());
    }

    private static boolean areEqual(CompoundDecimalField actual, CompoundDecimalField instr) {
        return areSameFieldInstructions(actual, instr) && areSameOperators(actual.getExponent(), instr.getExponent()) &&
            areSameOperators(actual.getMantissa(), instr.getMantissa());
    }

    private static boolean areEqual(SimpleDecimalField actual, SimpleDecimalField instr) {
        return areSameFieldInstructions(actual, instr) && areSameOperators(actual.getOperator(), instr.getOperator());
    }

    private static boolean areEqual(Group actual, Group instr) {
        return areSameFieldInstructions(actual, instr) && areSameReferences(actual.getTypeRef(), instr.getTypeRef()) &&
            areSameInstructions(actual.getInstructions(), instr.getInstructions());
    }

    private static boolean areEqual(Sequence actual, Sequence instr) {
        return areSameFieldInstructions(actual, instr) && areSameReferences(actual.getTypeRef(), instr.getTypeRef()) &&
            areSameInstructions(actual.getInstructions(), instr.getInstructions()) &&
            (actual.getLength() == instr.getLength() ||
                (areSameIdentities(actual.getLength().getFieldId(), instr.getLength().getFieldId()) &&
                    areSameOperators(actual.getLength().getOperator(), instr.getLength().getOperator())));
    }

    private static boolean areSameFieldInstructions(FieldInstruction actual, FieldInstruction instr) {
        return areSameIdentities(actual.getFieldId(), instr.getFieldId()) &&
            actual.isOptional() == instr.isOptional();
    }
}
