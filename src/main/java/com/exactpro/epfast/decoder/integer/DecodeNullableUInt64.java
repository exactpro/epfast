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

package com.exactpro.epfast.decoder.integer;

import com.exactpro.epfast.decoder.OverflowException;
import io.netty.buffer.ByteBuf;

import java.math.BigInteger;

public final class DecodeNullableUInt64 extends DecodeInteger {

    private static final long POSITIVE_LIMIT = 0x02000000_00000000L;

    private byte[] bytes = new byte[8];

    private boolean isUInt64Limit;

    private long value;

    public void decode(ByteBuf buf) {
        reset();
        value = 0;
        isUInt64Limit = false;
        int readerIndex = buf.readerIndex();
        int readLimit = buf.writerIndex();
        int oneByte = buf.getByte(readerIndex++);
        accumulate(oneByte);
        if (oneByte < 0) {
            buf.readerIndex(readerIndex);
            return;
        }
        if (readerIndex < readLimit) {
            checkOverlong(buf.getByte(readerIndex)); //check second byte
            do {
                accumulate(buf.getByte(readerIndex++));
            } while (!ready && readerIndex < readLimit);
        } else {
            checkForSignExtension = true;
        }
        buf.readerIndex(readerIndex);
    }

    public void continueDecode(ByteBuf buf) {
        int readerIndex = buf.readerIndex();
        int readLimit = buf.writerIndex();
        if (checkForSignExtension) {
            checkOverlong(buf.getByte(readerIndex)); //continue checking
            checkForSignExtension = false;
        }
        do {
            accumulate(buf.getByte(readerIndex++));
        } while (!ready && readerIndex < readLimit);
        buf.readerIndex(readerIndex);
    }

    public BigInteger getValue() throws OverflowException {
        if (overflow) {
            throw new OverflowException("UInt64 Overflow");
        } else if (value == 0) {
            return null;
        } else {
            if (isUInt64Limit) {
                longToBytes(-1L, bytes);
                return new BigInteger(1, bytes);
            } else {
                longToBytes(value - 1, bytes);
                return new BigInteger(1, bytes);
            }
        }
    }

    private void accumulate(int oneByte) {
        if (oneByte < 0) { // if stop bit is set
            oneByte &= CLEAR_STOP_BIT_MASK;
            ready = true;
        }
        if (value < POSITIVE_LIMIT) {
            value = (value << 7) | oneByte;
        } else if (value == POSITIVE_LIMIT && oneByte == 0 && ready) {
            isUInt64Limit = true;
        } else {
            overflow = true;
        }
    }

    private void checkOverlong(int secondByte) {
        overlong = value == 0 && ((secondByte & SIGN_BIT_MASK) == 0);
    }
}
