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

package com.exactpro.epfast.decoder.ascii;

import com.exactpro.epfast.decoder.OverflowException;
import com.exactpro.epfast.decoder.message.DecoderState;
import com.exactpro.epfast.decoder.message.UnionRegister;

public final class DecodeMandatoryAsciiString extends DecodeAsciiString {

    public DecodeMandatoryAsciiString() {
        this(false);
    }

    public DecodeMandatoryAsciiString(boolean checkOverlong) {
        super(checkOverlong);
    }

    @Override
    public void setRegisterValue(UnionRegister register) {
        if (stringBuilder.length() >= MAX_ALLOWED_LENGTH) {
            register.isOverflow = true;
            //TODO what to do with overflow information ?
            //throw new OverflowException("String is longer than allowed");
        }
        if (zeroCount < stringBuilder.length()) {
            if (zeroPreamble && checkOverlong) {
                //TODO set register flag
                register.isOverflow = true;
                //throw new OverflowException("String with zero preamble can't contain any value except 0");
            } else {
                register.stringValue = stringBuilder.toString();
            }
        } else if (zeroCount == 1) {
            register.stringValue = "";
        } else {
            stringBuilder.setLength(zeroCount - 1);
            register.stringValue = stringBuilder.toString();
        }
    }

    @Override
    public String getValue() throws OverflowException {
        if (stringBuilder.length() >= MAX_ALLOWED_LENGTH) {
            throw new OverflowException("String is longer than allowed");
        }
        if (zeroCount < stringBuilder.length()) {
            if (zeroPreamble && checkOverlong) {
                throw new OverflowException("String with zero preamble can't contain any value except 0");
            } else {
                return stringBuilder.toString();
            }
        } else if (zeroCount == 1) {
            return "";
        } else {
            stringBuilder.setLength(zeroCount - 1);
            return stringBuilder.toString();
        }
    }
}

