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

package com.exactpro.epfast.template;

public class Dictionary {

    private final String name;

    public static final Dictionary TEMPLATE = new Dictionary("template");

    public static final Dictionary TYPE = new Dictionary("type");

    public static final Dictionary GLOBAL = new Dictionary("global");

    public static Dictionary getDictionary(String name) {
        if (TEMPLATE.getName().equals(name)) {
            return TEMPLATE;
        } else if (TYPE.getName().equals(name)) {
            return TYPE;
        } else if (GLOBAL.getName().equals(name)) {
            return GLOBAL;
        }
        return new Dictionary(name);
    }

    private Dictionary(String name) {
        if (name == null) {
            throw new NullPointerException("name cannot be null");
        }
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Dictionary) && getName().equals(((Dictionary) o).getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
