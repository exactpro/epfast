package com.exactpro.epfast.template.assertions;

import com.exactpro.epfast.template.Identity;
import com.exactpro.epfast.template.Reference;
import com.exactpro.epfast.template.Template;
import org.assertj.core.api.AbstractAssert;

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
        if (!areEqualReferences(actual.getTypeRef(), template.getTypeRef()) ||
            !areEqualIdentities(actual.getTemplateId(), template.getTemplateId())) {
            failWithMessage("Not The Same Template");
        }
        return this;
    }

    private boolean areEqualReferences(Reference actual, Reference ref) {
        return Objects.equals(actual.getName(), ref.getName()) &&
            Objects.equals(actual.getNamespace(), ref.getNamespace());
    }

    private boolean areEqualIdentities(Identity actual, Identity id) {
        return areEqualReferences(actual, id) &&
            Objects.equals(actual.getAuxiliaryId(), id.getAuxiliaryId());
    }
}
