package com.exactpro.epfast.template.assertion;

import com.exactpro.epfast.template.*;

import java.util.Objects;

import static com.exactpro.epfast.template.assertion.TemplateAssert.areSameReferences;

public class OpEqualityChecker {

    static boolean areSameOperators(FieldOperator actual, FieldOperator operator) {
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
