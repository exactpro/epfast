package com.exactpro.epfast.template;

import org.assertj.core.api.AbstractAssert;

import java.util.List;
import java.util.Objects;

public class TemplateAssert extends AbstractAssert<TemplateAssert, Template> {

    public TemplateAssert(Template actual) {
        super(actual, TemplateAssert.class);
    }

    public static TemplateAssert assertThat(Template actual) {
        return new TemplateAssert(actual);
    }

    public TemplateAssert isSameTemplateAs(Template template) {
        isNotNull();
        if (!areSameReferences(actual.getTypeRef(), template.getTypeRef()) ||
            !areSameIdentities(actual.getTemplateId(), template.getTemplateId()) ||
            !areSameInstructions(actual.getInstructions(), template.getInstructions())) {
            failWithMessage("Not The Same Template");
        }
        return this;
    }

    private static boolean areSameReferences(Reference actual, Reference ref) {
        return actual == ref || (Objects.equals(actual.getName(), ref.getName()) &&
            Objects.equals(actual.getNamespace(), ref.getNamespace()));
    }

    private static boolean areSameIdentities(Identity actual, Identity id) {
        return actual == id || (areSameReferences(actual, id) &&
            Objects.equals(actual.getAuxiliaryId(), id.getAuxiliaryId()));
    }

    // Check Instructions

    private static boolean areSameInstructions(List<? extends Instruction> actual, List<? extends Instruction> instr) {
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

    // Check Operators

    private static boolean areSameOperators(FieldOperator actual, FieldOperator operator) {
        if (actual == operator) {
            return true;
        }
        if (actual.getClass() != operator.getClass()) {
            return false;
        }
        switch (actual.getClass().getSimpleName()) {
            case "ConstantOperator":
                return constantEquality((ConstantOperator) actual, (ConstantOperator) operator);
            case "CopyOperator":
                return copyEquality((CopyOperator) actual, (CopyOperator) operator);
            case "DefaultOperator":
                return defaultEquality((DefaultOperator) actual, (DefaultOperator) operator);
            case "DeltaOperator":
                return deltaEquality((DeltaOperator) actual, (DeltaOperator) operator);
            case "IncrementOperator":
                return incrementEquality((IncrementOperator) actual, (IncrementOperator) operator);
            case "TailOperator":
                return tailEquality((TailOperator) actual, (TailOperator) operator);
            default:
                return false;
        }
    }

    private static boolean constantEquality(ConstantOperator actual, ConstantOperator operator) {
        return fieldOpEquality(actual, operator);
    }

    private static boolean copyEquality(CopyOperator actual, CopyOperator operator) {
        return fieldOpEquality(actual, operator) && actual.getDictionary().equals(operator.getDictionary()) &&
            areSameReferences(actual.getDictionaryKey(), operator.getDictionaryKey());
    }

    private static boolean defaultEquality(DefaultOperator actual, DefaultOperator operator) {
        return fieldOpEquality(actual, operator);
    }

    private static boolean deltaEquality(DeltaOperator actual, DeltaOperator operator) {
        return fieldOpEquality(actual, operator) && actual.getDictionary().equals(operator.getDictionary()) &&
            areSameReferences(actual.getDictionaryKey(), operator.getDictionaryKey());
    }

    private static boolean incrementEquality(IncrementOperator actual, IncrementOperator operator) {
        return fieldOpEquality(actual, operator) && actual.getDictionary().equals(operator.getDictionary()) &&
            areSameReferences(actual.getDictionaryKey(), operator.getDictionaryKey());
    }

    private static boolean tailEquality(TailOperator actual, TailOperator operator) {
        return fieldOpEquality(actual, operator) && actual.getDictionary().equals(operator.getDictionary()) &&
            areSameReferences(actual.getDictionaryKey(), operator.getDictionaryKey());
    }

    private static boolean fieldOpEquality(FieldOperator actual, FieldOperator fieldOp) {
        return Objects.equals(actual.getInitialValue(), fieldOp.getInitialValue());
    }
}
