package com.exactpro.epfast.template.assertion;

import com.exactpro.epfast.template.Template;

import java.util.List;

import static com.exactpro.epfast.template.assertion.TemplateAssert.assertThat;

public class TemplatesComparison {

    public static boolean areEqualTemplateLists(List<? extends Template> actual, List<? extends Template> expected) {
        if (actual.size() != expected.size()) {
            return false;
        }
        for (int i = 0; i < actual.size(); i++) {
            assertThat(actual.get(i)).isEqualToTemplate(expected.get(i));
        }
        return true;
    }
}
