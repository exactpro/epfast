/*
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
 */

package com.exactpro.epfast.annotation.processing;

class TypeName {
    private String packageName;

    private String className;

    TypeName(String fullTypeName) {
        int lastDotIndex = fullTypeName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            this.packageName = "";
            this.className = fullTypeName;
        }  else {
            this.packageName = fullTypeName.substring(0, lastDotIndex);
            this.className = fullTypeName.substring(lastDotIndex + 1);
        }
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public String toString() {
        return packageName.isEmpty() ? className : packageName + "." + className;
    }
}
