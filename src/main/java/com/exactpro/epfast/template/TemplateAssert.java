package com.exactpro.epfast.template;

import org.assertj.core.api.AbstractAssert;

public class TemplateAssert extends AbstractAssert<TemplateAssert, Template> {

    public TemplateAssert(Template actual) {
        super(actual, TemplateAssert.class);
    }

    public static TemplateAssert assertThat(Template actual) {
        return new TemplateAssert(actual);
    }
}
