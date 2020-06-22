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
