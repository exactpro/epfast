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

import com.exactpro.epfast.decoder.StreamDecoderCommand;
import com.exactpro.epfast.decoder.message.UnionRegister;
import io.netty.buffer.ByteBuf;

public abstract class DecodeAsciiString extends StreamDecoderCommand {

    final StringBuilder stringBuilder = new StringBuilder();

    private boolean ready;

    boolean decodingPreamble = true;

    final boolean checkOverlong;

    int zeroCount;

    static final int MAX_ALLOWED_LENGTH = 0x20000;

    DecodeAsciiString(boolean checkOverlong) {
        this.checkOverlong = checkOverlong;
    }

    @Override
    public int decode(ByteBuf buf, UnionRegister register) {
        int readerIndex = buf.readerIndex();
        int readLimit = buf.writerIndex();
        while (decodingPreamble && !ready && (readerIndex < readLimit)) {
            accumulateValue(getPreambleByte(readerIndex++, buf));
        }
        while (!ready && (readerIndex < readLimit)) {
            accumulateValue(getByte(readerIndex++, buf));
        }
        buf.readerIndex(readerIndex);
        if (ready) {
            setResult(register);
            reset();
            return FINISHED;
        } else {
            return MORE_DATA_NEEDED;
        }
    }

    public abstract void setResult(UnionRegister unionRegister);

    private int getPreambleByte(int index, ByteBuf buf) {
        int aByte = getByte(index, buf);
        if (aByte == 0) {
            ++zeroCount;
        } else {
            decodingPreamble = false;
        }
        return aByte;
    }

    private int getByte(int index, ByteBuf buf) {
        int aByte = buf.getByte(index);
        if (aByte < 0) { // if stop bit is set
            aByte &= CLEAR_STOP_BIT_MASK;
            ready = true;
        }
        return aByte;
    }

    private void accumulateValue(int aByte) {
        if (stringBuilder.length() < MAX_ALLOWED_LENGTH) {
            stringBuilder.append((char) aByte);
        }
    }

    public final void reset() {
        stringBuilder.setLength(0);
        ready = false;
        decodingPreamble = true;
        zeroCount = 0;
    }
}
