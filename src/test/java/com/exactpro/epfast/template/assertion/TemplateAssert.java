package com.exactpro.epfast.template.assertion;

import com.exactpro.epfast.template.*;
import org.assertj.core.api.AbstractAssert;

import java.util.Objects;

import static com.exactpro.epfast.template.assertion.InstrEqualityChecker.areSameInstructions;

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

    static boolean areSameReferences(Reference actual, Reference ref) {
        return actual == ref || (Objects.equals(actual.getName(), ref.getName()) &&
            Objects.equals(actual.getNamespace(), ref.getNamespace()));
    }

    static boolean areSameIdentities(Identity actual, Identity id) {
        return actual == id || (areSameReferences(actual, id) &&
            Objects.equals(actual.getAuxiliaryId(), id.getAuxiliaryId()));
    }
}
