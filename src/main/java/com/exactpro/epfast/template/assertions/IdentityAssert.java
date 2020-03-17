package com.exactpro.epfast.template.assertions;

import com.exactpro.epfast.template.Identity;
import org.assertj.core.api.AbstractAssert;

import static com.exactpro.epfast.template.assertions.Equality.areNotEqual;

public class IdentityAssert extends AbstractAssert<IdentityAssert, Identity> {

    public IdentityAssert(Identity actual) {
        super(actual, IdentityAssert.class);
    }

    public static IdentityAssert assertThat(Identity actual) {
        return new IdentityAssert(actual);
    }

    public IdentityAssert isSameIdentityAs(Identity identity) {
        isNotNull();
        try {
            ReferenceAssert.assertThat(actual).isSameReferenceAs(identity);
            if (areNotEqual(actual.getAuxiliaryId(), identity.getAuxiliaryId())) {
                throw new AssertionError();
            }
        } catch (AssertionError error) {
            failWithMessage("Not The Same Identity");
        }
        return this;
    }
}
