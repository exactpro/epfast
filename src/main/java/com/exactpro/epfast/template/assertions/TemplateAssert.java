package com.exactpro.epfast.template.assertions;

import com.exactpro.epfast.template.Template;
import org.assertj.core.api.AbstractAssert;

public class TemplateAssert extends AbstractAssert<TemplateAssert, Template> {

    public TemplateAssert(Template actual) {
        super(actual, TemplateAssert.class);
    }

    public static TemplateAssert assertThat(Template actual) {
        return new TemplateAssert(actual);
    }

    public TemplateAssert isSameTemplateAs(Template template) {
        isNotNull();
        try {
            ReferenceAssert.assertThat(actual.getTypeRef()).isSameReferenceAs(template.getTypeRef());
            IdentityAssert.assertThat(actual.getTemplateId()).isSameIdentityAs(template.getTemplateId());
        } catch (AssertionError error) {
            failWithMessage("Not The Same Template");
        }
        return this;
    }
}
