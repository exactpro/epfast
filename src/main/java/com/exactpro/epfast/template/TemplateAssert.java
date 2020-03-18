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
            !areSameIdentities(actual.getTemplateId(), template.getTemplateId())) {
            failWithMessage("Not The Same Template");
        }
        return this;
    }

    private boolean areSameReferences(Reference actual, Reference ref) {
        return actual == ref || (Objects.equals(actual.getName(), ref.getName()) &&
            Objects.equals(actual.getNamespace(), ref.getNamespace()));
    }

    private boolean areSameIdentities(Identity actual, Identity id) {
        return actual == id || (areSameReferences(actual, id) &&
            Objects.equals(actual.getAuxiliaryId(), id.getAuxiliaryId()));
    }

    private boolean areSameInstructions(List<? extends Instruction> actual, List<? extends Instruction> instr) {
        return actual == instr;
    }
}
