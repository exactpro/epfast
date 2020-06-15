/******************************************************************************
 * Copyright 2019-2020 Exactpro (Exactpro Systems Limited)
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

package com.exactpro.epfast.decoder.message;

import com.exactpro.epfast.decoder.IMessage;
import com.exactpro.epfast.template.Reference;

import java.util.HashMap;

public class FastMessage implements IMessage {

    private Reference type;

    // TODO change to HashMap<Reference, Object>
    private HashMap<String, Object> fields = new HashMap<>();

    public FastMessage(Reference type) {
        this.type = type;
    }

    @Override
    public void setField(String name, Object val) {
        fields.put(name, val);
    }

    @Override
    public Object getField(String name) {
        return fields.get(name);
    }
}
