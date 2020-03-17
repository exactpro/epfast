package com.exactpro.epfast.template.assertions;

import com.exactpro.epfast.template.Reference;
import org.assertj.core.api.AbstractAssert;

import static com.exactpro.epfast.template.assertions.Equality.areNotEqual;

public class ReferenceAssert extends AbstractAssert<ReferenceAssert, Reference> {

    public ReferenceAssert(Reference actual) {
        super(actual, ReferenceAssert.class);
    }

    public static ReferenceAssert assertThat(Reference actual) {
        return new ReferenceAssert(actual);
    }

    public ReferenceAssert isSameReferenceAs(Reference typeRef) {
        isNotNull();
        if (areNotEqual(actual.getName(), typeRef.getName()) ||
            areNotEqual(actual.getNamespace(), typeRef.getNamespace())) {
            failWithMessage("Not The Same Reference");
        }
        return this;
    }
}
