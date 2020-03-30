package com.exactpro.epfast.template.assertion;

import com.exactpro.epfast.template.*;
import org.assertj.core.api.AbstractAssert;

import java.util.Objects;

import static com.exactpro.epfast.template.assertion.InstrEqualityChecker.areEqualInstructionLists;

public class TemplateAssert extends AbstractAssert<TemplateAssert, Template> {

    public TemplateAssert(Template actual) {
        super(actual, TemplateAssert.class);
    }

    public static TemplateAssert assertThat(Template actual) {
        return new TemplateAssert(actual);
    }

    public TemplateAssert isEqualToTemplate(Template template) {
        isNotNull();
        if (!areEqualReferences(actual.getTypeRef(), template.getTypeRef()) ||
            !areEqualIdentities(actual.getTemplateId(), template.getTemplateId()) ||
            !areEqualInstructionLists(actual.getInstructions(), template.getInstructions())) {
            failWithMessage("FAST templates aren't equal.");
        }
        return this;
    }

    static boolean areEqualReferences(Reference actual, Reference expected) {
        return actual == expected || (Objects.equals(actual.getName(), expected.getName()) &&
            Objects.equals(actual.getNamespace(), expected.getNamespace()));
    }

    static boolean areEqualIdentities(Identity actual, Identity expected) {
        return areEqualReferences(actual, expected) &&
            Objects.equals(actual.getAuxiliaryId(), expected.getAuxiliaryId());
    }
}
