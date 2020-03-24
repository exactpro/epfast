package com.exactpro.epfast.template.assertion;

import com.exactpro.epfast.template.*;

import java.util.List;
import java.util.Objects;

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
                if (actualItem.getClass() != instrItem.getClass()) {
                    return false;
                }

                switch (actualItem.getClass().getSimpleName()) {
                    case "TemplateRef":
                        if (!templateRefEquality((TemplateRef) actualItem, (TemplateRef) instrItem)) {
                            return false;
                        }
                        break;
                    case "Int32Field":
                        if (!int32Equality((Int32Field) actualItem, (Int32Field) instrItem)) {
                            return false;
                        }
                        break;
                    case "Int64Field":
                        if (!int64Equality((Int64Field) actualItem, (Int64Field) instrItem)) {
                            return false;
                        }
                        break;
                    case "UInt32Field":
                        if (!uInt32Equality((UInt32Field) actualItem, (UInt32Field) instrItem)) {
                            return false;
                        }
                        break;
                    case "UInt64Field":
                        if (!uInt64Equality((UInt64Field) actualItem, (UInt64Field) instrItem)) {
                            return false;
                        }
                        break;
                    case "AsciiStringField":
                        if (!asciiStringEquality((AsciiStringField) actualItem, (AsciiStringField) instrItem)) {
                            return false;
                        }
                        break;
                    case "UnicodeStringField":
                        if (!unicodeStringEquality((UnicodeStringField) actualItem, (UnicodeStringField) instrItem)) {
                            return false;
                        }
                        break;
                    case "ByteVectorField":
                        if (!byteVectorEquality((ByteVectorField) actualItem, (ByteVectorField) instrItem)) {
                            return false;
                        }
                        break;
                    case "CompoundDecimalField":
                        if (!compoundDecEquality((CompoundDecimalField) actualItem, (CompoundDecimalField) instrItem)) {
                            return false;
                        }
                        break;
                    case "SimpleDecimalField":
                        if (!simpleDecEquality((SimpleDecimalField) actualItem, (SimpleDecimalField) instrItem)) {
                            return false;
                        }
                        break;
                    case "Group":
                        if (!groupEquality((Group) actualItem, (Group) instrItem)) {
                            return false;
                        }
                        break;
                    case "Sequence":
                        if (!sequenceEquality((Sequence) actualItem, (Sequence) instrItem)) {
                            return false;
                        }
                        break;
                    default:
                        return false;
                }
            }
        }
        return true;
    }

    private static boolean templateRefEquality(TemplateRef actual, TemplateRef instr) {
        return areSameReferences(actual.getTemplateRef(), instr.getTemplateRef());
    }

    private static boolean int32Equality(Int32Field actual, Int32Field instr) {
        return fieldInstrEquality(actual, instr) && areSameOperators(actual.getOperator(), instr.getOperator());
    }

    private static boolean int64Equality(Int64Field actual, Int64Field instr) {
        return fieldInstrEquality(actual, instr) && areSameOperators(actual.getOperator(), instr.getOperator());
    }

    private static boolean uInt32Equality(UInt32Field actual, UInt32Field instr) {
        return fieldInstrEquality(actual, instr) && areSameOperators(actual.getOperator(), instr.getOperator());
    }

    private static boolean uInt64Equality(UInt64Field actual, UInt64Field instr) {
        return fieldInstrEquality(actual, instr) && areSameOperators(actual.getOperator(), instr.getOperator());
    }

    private static boolean asciiStringEquality(AsciiStringField actual, AsciiStringField instr) {
        return fieldInstrEquality(actual, instr) && areSameOperators(actual.getOperator(), instr.getOperator());
    }

    private static boolean unicodeStringEquality(UnicodeStringField actual, UnicodeStringField instr) {
        return fieldInstrEquality(actual, instr) && areSameOperators(actual.getOperator(), instr.getOperator()) &&
            areSameIdentities(actual.getLengthFieldId(), instr.getLengthFieldId());
    }

    private static boolean byteVectorEquality(ByteVectorField actual, ByteVectorField instr) {
        return fieldInstrEquality(actual, instr) && areSameOperators(actual.getOperator(), instr.getOperator()) &&
            areSameIdentities(actual.getLengthFieldId(), instr.getLengthFieldId());
    }

    private static boolean compoundDecEquality(CompoundDecimalField actual, CompoundDecimalField instr) {
        return fieldInstrEquality(actual, instr) && areSameOperators(actual.getExponent(), instr.getExponent()) &&
            areSameOperators(actual.getMantissa(), instr.getMantissa());
    }

    private static boolean simpleDecEquality(SimpleDecimalField actual, SimpleDecimalField instr) {
        return fieldInstrEquality(actual, instr) && areSameOperators(actual.getOperator(), instr.getOperator());
    }

    private static boolean groupEquality(Group actual, Group instr) {
        return fieldInstrEquality(actual, instr) && areSameReferences(actual.getTypeRef(), instr.getTypeRef()) &&
            areSameInstructions(actual.getInstructions(), instr.getInstructions());
    }

    private static boolean sequenceEquality(Sequence actual, Sequence instr) {
        return fieldInstrEquality(actual, instr) && areSameReferences(actual.getTypeRef(), instr.getTypeRef()) &&
            areSameInstructions(actual.getInstructions(), instr.getInstructions()) &&
            (Objects.equals(actual.getLength(), instr.getLength()) ||
                (areSameIdentities(actual.getLength().getFieldId(), instr.getLength().getFieldId()) &&
                    areSameOperators(actual.getLength().getOperator(), instr.getLength().getOperator())));
    }

    private static boolean fieldInstrEquality(FieldInstruction actual, FieldInstruction instr) {
        return areSameIdentities(actual.getFieldId(), instr.getFieldId()) &&
            actual.isOptional() == instr.isOptional();
    }
}
