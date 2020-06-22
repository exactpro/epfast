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

import com.exactpro.epfast.decoder.IDecodeContext;
import com.exactpro.epfast.decoder.OverflowException;
import io.netty.buffer.ByteBuf;

public abstract class DecodeAsciiString implements IDecodeContext {

    final StringBuilder stringBuilder = new StringBuilder();

    private boolean ready;

    boolean zeroPreamble;

    final boolean checkOverlong;

    int zeroCount;

    static final int MAX_ALLOWED_LENGTH = 0x20000;

    DecodeAsciiString(boolean checkOverlong) {
        this.checkOverlong = checkOverlong;
    }

    public void decode(ByteBuf buf) {
        reset();
        int readerIndex = buf.readerIndex();
        int readLimit = buf.writerIndex();
        if (buf.getByte(readerIndex) == 0) {
            zeroPreamble = true;
        }
        accumulateValue(buf.getByte(readerIndex++));
        while ((readerIndex < readLimit) && !ready) {
            accumulateValue(buf.getByte(readerIndex++));
        }
        buf.readerIndex(readerIndex);
    }

    public void continueDecode(ByteBuf buf) {
        int readerIndex = buf.readerIndex();
        int readLimit = buf.writerIndex();
        while ((readerIndex < readLimit) && !ready) {
            accumulateValue(buf.getByte(readerIndex++));
        }
        buf.readerIndex(readerIndex);
    }

    public abstract String getValue() throws OverflowException;

    public boolean isReady() {
        return ready;
    }

    public boolean isOverlong() {
        return (zeroPreamble && (zeroCount < stringBuilder.length()));
    }

    private void accumulateValue(int oneByte) {
        if (oneByte < 0) { // if stop bit is set
            oneByte &= CLEAR_STOP_BIT_MASK;
            ready = true;
        }
        if (oneByte == 0) {
            zeroCount++;
        }
        if (stringBuilder.length() < MAX_ALLOWED_LENGTH) {
            stringBuilder.append((char) oneByte);
        }
    }

    public final void reset() {
        stringBuilder.setLength(0);
        ready = false;
        zeroCount = 0;
        zeroPreamble = false;
    }
}
