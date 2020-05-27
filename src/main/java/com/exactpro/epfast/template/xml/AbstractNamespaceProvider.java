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

package com.exactpro.epfast.template.xml;

import com.exactpro.epfast.template.Dictionary;

public abstract class AbstractNamespaceProvider extends DelegatingNamespaceProvider {

    private String templateNs;

    private String applicationNs;

    private Dictionary dictionary;

    @Override
    public String getTemplateNamespace() {
        if (templateNs != null) {
            return templateNs;
        }
        return super.getTemplateNamespace();
    }

    @Override
    public String getApplicationNamespace() {
        if (applicationNs != null) {
            return applicationNs;
        }
        return super.getApplicationNamespace();
    }

    @Override
    public Dictionary getDictionary() {
        if (dictionary != null) {
            return dictionary;
        }
        return super.getDictionary();
    }

    protected void setTemplateNs(String templateNs) {
        this.templateNs = templateNs;
    }

    protected void setApplicationNs(String ns) {
        this.applicationNs = ns;
    }

    protected void setDictionaryName(String dictionary) {
        this.dictionary = Dictionary.getDictionary(dictionary);
    }
}
