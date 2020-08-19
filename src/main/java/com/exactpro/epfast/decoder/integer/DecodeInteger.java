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

package com.exactpro.epfast.decoder.integer;

import com.exactpro.epfast.decoder.IDecodeContext;
import io.netty.buffer.ByteBuf;

public abstract class DecodeInteger extends IDecodeContext {

    protected static final int SIGN_BIT_MASK = 0b01000000;

    protected boolean ready;

    protected boolean overflow;

    protected boolean overlong;

    protected boolean checkForSignExtension = true;

    protected boolean inProgress;

    protected int bytesRead;

    protected final void reset() {
        inProgress = false;
        bytesRead = 0;
        ready = false;
        overflow = false;
        overlong = false;
        checkForSignExtension = true;
    }

    protected int getByte(ByteBuf buf, int index) {
        int oneByte = buf.getByte(index);
        ++bytesRead;
        if (oneByte < 0) { // if stop bit is set
            ready = true;
            return oneByte & CLEAR_STOP_BIT_MASK;
        } else {
            return oneByte;
        }
    }

    protected static void longToBytes(long value, byte[] bytes) {
        for (int i = 7; i >= 0; --i) {
            bytes[i] = (byte) value;
            value >>>= 8;
        }
    }
}
