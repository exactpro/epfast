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

import com.exactpro.epfast.decoder.message.UnionRegister;
import io.netty.buffer.ByteBuf;

import java.math.BigInteger;

public final class DecodeMandatoryUInt64 extends DecodeInteger {

    private static final long OVERFLOW_MASK = 0xFE00000000000000L;

    private byte[] bytes = new byte[8];

    private long value;

    @Override
    public int decode(ByteBuf buf, UnionRegister register) {
        int readerIndex = buf.readerIndex();
        int readLimit = buf.writerIndex();
        if (!inProgress) {
            value = 0;
            inProgress = true;
            int oneByte = buf.getByte(readerIndex++);
            accumulate(oneByte);
            if (oneByte < 0) {
                setResult(register);
                buf.readerIndex(readerIndex);
                return FINISHED;
            }
            if (readerIndex < readLimit) {
                checkOverlong(buf.getByte(readerIndex)); //check second byte
                checkForSignExtension = false;
                do {
                    accumulate(buf.getByte(readerIndex++));
                } while (!ready && readerIndex < readLimit);
            }
        } else {
            if (checkForSignExtension) {
                checkOverlong(buf.getByte(readerIndex)); //continue checking
                checkForSignExtension = false;
            }
            do {
                accumulate(buf.getByte(readerIndex++));
            } while (!ready && readerIndex < readLimit);
        }
        buf.readerIndex(readerIndex);
        if (ready) {
            setResult(register);
            return FINISHED;
        } else {
            return MORE_DATA_NEEDED;
        }
    }

    private void setResult(UnionRegister register) {
        inProgress = false;
        register.isOverlong = overlong;
        register.isNull = false;
        if (overflow) {
            register.isOverflow = true;
            register.infoMessage = "UInt32 Overflow";
        } else {
            longToBytes(value, bytes);
            register.isOverflow = false;
            register.uInt64Value = new BigInteger(1, bytes);
        }
        reset();
    }

    private void accumulate(int oneByte) {
        if (oneByte < 0) { // if stop bit is set
            oneByte &= CLEAR_STOP_BIT_MASK;
            ready = true;
        }
        if ((value & OVERFLOW_MASK) == 0) {
            value = (value << 7) | oneByte;
        } else {
            overflow = true;
        }
    }

    private void checkOverlong(int secondByte) {
        overlong = value == 0 && ((secondByte & SIGN_BIT_MASK) == 0);
    }
}
