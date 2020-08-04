/*
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
 */

package com.exactpro.epfast.decoder.message;

import com.exactpro.epfast.decoder.presencemap.PresenceMap;

import java.math.BigDecimal;
import java.math.BigInteger;

// Logically it's one register. It's up to application to ensure that the right field is used for read / write.
public class UnionRegister {

    public boolean isNull;

    public boolean isOverflow;

    public boolean isOverlong;

    public int int32Value;

    public long uInt32Value;

    public String stringValue;

    public long int64Value;

    public BigInteger unsignedInt64Value;

    public BigDecimal decimalValue;

    public byte[] byteVectorValue;

    public Object applicationValue;

    public String errorMessage;

    public PresenceMap presenceMap;
}

