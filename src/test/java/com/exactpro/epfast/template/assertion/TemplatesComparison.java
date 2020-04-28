package com.exactpro.epfast.template.assertion;

import com.exactpro.epfast.template.Template;

import java.util.List;

import static com.exactpro.epfast.template.assertion.TemplateAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

public class TemplatesComparison {

    public static void areEqualTemplateLists(List<? extends Template> actual, List<? extends Template> expected) {
        assertThat(actual).hasSameSizeAs(expected);
        for (int i = 0; i < actual.size(); i++) {
            assertThat(actual.get(i)).isEqualToTemplate(expected.get(i));
        }
    }
}
