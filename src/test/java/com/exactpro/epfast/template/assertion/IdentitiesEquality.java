package com.exactpro.epfast.template.assertion;

import com.exactpro.epfast.template.Identity;
import com.exactpro.epfast.template.Reference;

import java.util.Objects;

public class IdentitiesEquality {

    static boolean areEqualReferences(Reference actual, Reference expected) {
        return actual == expected || (Objects.equals(actual.getName(), expected.getName()) &&
            Objects.equals(actual.getNamespace(), expected.getNamespace()));
    }

    static boolean areEqualIdentities(Identity actual, Identity expected) {
        return areEqualReferences(actual, expected) &&
            Objects.equals(actual.getAuxiliaryId(), expected.getAuxiliaryId());
    }

    static boolean areEqualLengthIdentities(Identity actual, Identity expected) {
        return Objects.equals(actual.getName(), expected.getName()) &&
            Objects.equals(actual.getAuxiliaryId(), expected.getAuxiliaryId());
    }
}
