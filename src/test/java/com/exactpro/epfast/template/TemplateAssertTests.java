/*
 * Copyright 2020 Exactpro (Exactpro Systems Limited)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.exactpro.epfast.template;

import com.exactpro.epfast.template.assertion.TemplateAssert;
import com.exactpro.epfast.template.simple.Identity;
import com.exactpro.epfast.template.simple.Reference;
import com.exactpro.epfast.template.simple.Template;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TemplateAssertTests {

    private Template template;

    private Template otherTemplate;

    public TemplateAssertTests() {
        template = new Template();
        otherTemplate = new Template();

        Reference reference = new Reference();
        Reference otherReference = new Reference();
        Identity identity = new Identity();
        Identity otherIdentity = new Identity();

        reference.setNamespace("namespace");
        otherReference.setNamespace("otherNamespace");
        identity.setName("name");
        otherIdentity.setName("otherName");
        identity.setAuxiliaryId("ID");
        otherIdentity.setAuxiliaryId("otherID");

        template.setTypeRef(reference);
        otherTemplate.setTypeRef(otherReference);
        template.setTemplateId(identity);
        otherTemplate.setTemplateId(otherIdentity);
    }

    @Test
    void testTemplateAssert() {
        TemplateAssert.assertThat(template).isEqualToTemplate(template);
        assertThatThrownBy(() -> TemplateAssert.assertThat(template).isEqualToTemplate(otherTemplate))
            .isInstanceOf(AssertionError.class);
    }
}
