package com.exactpro.epfast.template.assertion;

import com.exactpro.epfast.template.Template;
import org.assertj.core.api.AbstractAssert;

import static com.exactpro.epfast.template.assertion.IdentitiesEquality.*;
import static com.exactpro.epfast.template.assertion.InstructionsEquality.areEqualInstructionLists;

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
}
