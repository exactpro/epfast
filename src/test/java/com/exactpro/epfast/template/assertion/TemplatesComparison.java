/******************************************************************************
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
 ******************************************************************************/

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
