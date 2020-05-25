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

import com.exactpro.epfast.template.Identity;

public abstract class AbstractIdentity implements Identity {

    private String name;

    private String auxiliaryId;

    private final NamespaceProvider nsProvider;

    protected AbstractIdentity(NamespaceProvider nsProvider) {
        this.nsProvider = nsProvider;
    }

    protected NamespaceProvider getNamespaceProvider() {
        return nsProvider;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuxiliaryId(String auxiliaryId) {
        this.auxiliaryId = auxiliaryId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAuxiliaryId() {
        return auxiliaryId;
    }
}
