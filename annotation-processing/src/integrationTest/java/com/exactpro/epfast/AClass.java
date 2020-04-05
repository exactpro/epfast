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

package com.exactpro.epfast;

import com.exactpro.epfast.annotations.FastField;
import com.exactpro.epfast.annotations.FastType;

@FastType
public class AClass {

    private int superFoo;

    private String name;

    @FastField(name = "XXX")
    public void setFoo(int foo) {
        this.superFoo = foo;
    }

    @FastField(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    public int getSuperFoo() {
        return superFoo;
    }

    public String getName() {
        return name;
    }
}
