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

package com.exactpro.epfast.template.simple;

public class Identity extends Reference implements com.exactpro.epfast.template.Identity {

    private String auxiliaryId;

    public Identity() {
    }

    public Identity(String name) {
        super(name);
    }

    public Identity(String name, String namespace) {
        super(name, namespace);
    }

    public Identity(String name, String namespace, String auxiliaryId) {
        super(name, namespace);
        this.auxiliaryId = auxiliaryId;
    }

    @Override
    public String getAuxiliaryId() {
        return auxiliaryId;
    }

    public void setAuxiliaryId(String auxiliaryId) {
        this.auxiliaryId = auxiliaryId;
    }
}
