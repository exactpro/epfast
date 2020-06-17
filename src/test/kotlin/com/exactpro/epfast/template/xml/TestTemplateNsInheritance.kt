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

package com.exactpro.epfast.template.xml

import com.exactpro.epfast.template.assertion.TemplatesComparison.assertTemplateListsAreEqual
import com.exactpro.epfast.template.dsl.template
import org.junit.jupiter.api.Test

class TestTemplateNsInheritance {

    @Test
    fun `ensure namespace is inherited within template and templateRef`() {
        val expected = listOf(
            template("template1", "tempNS") {
                templateRef {
                    name = "templateRef"
                    namespace = "tempNS"
                }
            },
            template("template2", "NS") {
                templateRef {
                    name = "templateRef"
                    namespace = "NS"
                }
            }
        )
        val actual = readTemplatesFromString(
            """
            <templates templateNs="tempNS" xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
                <template name="template1" ns="ns">
                    <templateRef name="templateRef"/>
                </template>
                <template name="template2" templateNs="NS">
                    <templateRef name="templateRef"/>
                </template>
            </templates>
            """
        )
        assertTemplateListsAreEqual(actual, expected)
    }
}
