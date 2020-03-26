package com.exactpro.epfast.template.assertion;

import com.exactpro.epfast.template.*;

import java.util.Objects;

import static com.exactpro.epfast.template.assertion.TemplateAssert.areSameReferences;

public class OpEqualityChecker {

    static boolean areSameOperators(FieldOperator actual, FieldOperator operator) {
        if (actual == operator) {
            return true;
        }
        if (!actual.getClass().equals(operator.getClass())) {
            return false;
        }
        if (actual instanceof ConstantOperator) {
            return areEqual((ConstantOperator) actual, (ConstantOperator) operator);
        } else if (actual instanceof CopyOperator) {
            return areEqual((CopyOperator) actual, (CopyOperator) operator);
        } else if (actual instanceof DefaultOperator) {
            return areEqual((DefaultOperator) actual, (DefaultOperator) operator);
        } else if (actual instanceof DeltaOperator) {
            return areEqual((DeltaOperator) actual, (DeltaOperator) operator);
        } else if (actual instanceof IncrementOperator) {
            return areEqual((IncrementOperator) actual, (IncrementOperator) operator);
        } else if (actual instanceof TailOperator) {
            return areEqual((TailOperator) actual, (TailOperator) operator);
        } else {
            return false;
        }
    }

    private static boolean areEqual(ConstantOperator actual, ConstantOperator operator) {
        return areSameFieldOperators(actual, operator);
    }

    private static boolean areEqual(CopyOperator actual, CopyOperator operator) {
        return areSameFieldOperators(actual, operator) && actual.getDictionary().equals(operator.getDictionary()) &&
            areSameReferences(actual.getDictionaryKey(), operator.getDictionaryKey());
    }

    private static boolean areEqual(DefaultOperator actual, DefaultOperator operator) {
        return areSameFieldOperators(actual, operator);
    }

    private static boolean areEqual(DeltaOperator actual, DeltaOperator operator) {
        return areSameFieldOperators(actual, operator) && actual.getDictionary().equals(operator.getDictionary()) &&
            areSameReferences(actual.getDictionaryKey(), operator.getDictionaryKey());
    }

    private static boolean areEqual(IncrementOperator actual, IncrementOperator operator) {
        return areSameFieldOperators(actual, operator) && actual.getDictionary().equals(operator.getDictionary()) &&
            areSameReferences(actual.getDictionaryKey(), operator.getDictionaryKey());
    }

    private static boolean areEqual(TailOperator actual, TailOperator operator) {
        return areSameFieldOperators(actual, operator) && actual.getDictionary().equals(operator.getDictionary()) &&
            areSameReferences(actual.getDictionaryKey(), operator.getDictionaryKey());
    }

    private static boolean areSameFieldOperators(FieldOperator actual, FieldOperator fieldOp) {
        return Objects.equals(actual.getInitialValue(), fieldOp.getInitialValue());
    }
}
