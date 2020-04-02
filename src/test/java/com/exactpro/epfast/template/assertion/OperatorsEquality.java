package com.exactpro.epfast.template.assertion;

import com.exactpro.epfast.template.*;

import java.util.Objects;

import static com.exactpro.epfast.template.assertion.IdentitiesEquality.areEqualReferences;

public class OperatorsEquality {

    static boolean areEqualOperators(FieldOperator actual, FieldOperator expected) {
        if (actual == expected) {
            return true;
        }
        if ((actual instanceof ConstantOperator) && (expected instanceof ConstantOperator)) {
            return areEqual((ConstantOperator) actual, (ConstantOperator) expected);
        } else if ((actual instanceof CopyOperator) && (expected instanceof CopyOperator)) {
            return areEqual((CopyOperator) actual, (CopyOperator) expected);
        } else if ((actual instanceof DefaultOperator) && (expected instanceof DefaultOperator)) {
            return areEqual((DefaultOperator) actual, (DefaultOperator) expected);
        } else if ((actual instanceof DeltaOperator) && (expected instanceof DeltaOperator)) {
            return areEqual((DeltaOperator) actual, (DeltaOperator) expected);
        } else if ((actual instanceof IncrementOperator) && (expected instanceof IncrementOperator)) {
            return areEqual((IncrementOperator) actual, (IncrementOperator) expected);
        } else if ((actual instanceof TailOperator) && (expected instanceof TailOperator)) {
            return areEqual((TailOperator) actual, (TailOperator) expected);
        } else {
            return false;
        }
    }

    private static boolean areEqual(ConstantOperator actual, ConstantOperator expected) {
        return areEqualFieldOperators(actual, expected);
    }

    private static boolean areEqual(CopyOperator actual, CopyOperator expected) {
        return areEqualFieldOperators(actual, expected) && actual.getDictionary().equals(expected.getDictionary()) &&
            areEqualReferences(actual.getDictionaryKey(), expected.getDictionaryKey());
    }

    private static boolean areEqual(DefaultOperator actual, DefaultOperator expected) {
        return areEqualFieldOperators(actual, expected);
    }

    private static boolean areEqual(DeltaOperator actual, DeltaOperator expected) {
        return areEqualFieldOperators(actual, expected) && actual.getDictionary().equals(expected.getDictionary()) &&
            areEqualReferences(actual.getDictionaryKey(), expected.getDictionaryKey());
    }

    private static boolean areEqual(IncrementOperator actual, IncrementOperator expected) {
        return areEqualFieldOperators(actual, expected) && actual.getDictionary().equals(expected.getDictionary()) &&
            areEqualReferences(actual.getDictionaryKey(), expected.getDictionaryKey());
    }

    private static boolean areEqual(TailOperator actual, TailOperator expected) {
        return areEqualFieldOperators(actual, expected) && actual.getDictionary().equals(expected.getDictionary()) &&
            areEqualReferences(actual.getDictionaryKey(), expected.getDictionaryKey());
    }

    private static boolean areEqualFieldOperators(FieldOperator actual, FieldOperator expected) {
        return Objects.equals(actual.getInitialValue(), expected.getInitialValue());
    }
}
