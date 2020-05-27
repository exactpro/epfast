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

import com.exactpro.epfast.template.Reference;

public class TemplateIdReferenceImpl implements Reference {

    private String name;

    private String namespace;

    private NamespaceProvider nsProvider;

    public TemplateIdReferenceImpl(NamespaceProvider nsProvider) {
        this.nsProvider = nsProvider;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getNamespace() {
        if (namespace != null) {
            return namespace;
        }
        return nsProvider.getTemplateNamespace();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNamespace(String ns) {
        this.namespace = ns;
    }
}
