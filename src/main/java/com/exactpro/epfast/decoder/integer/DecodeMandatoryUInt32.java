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

public final class DecodeMandatoryUInt32 extends DecodeInteger {

    private static final int OVERFLOW_MASK = 0xFE000000;

    private int value;

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
                checkOverlong(buf.getByte(readerIndex), register); //check second byte
                do {
                    accumulate(buf.getByte(readerIndex++));
                } while (!ready && readerIndex < readLimit);
            } else {
                checkForSignExtension = true;
            }
        } else {
            if (checkForSignExtension) {
                checkOverlong(buf.getByte(readerIndex), register); //continue checking
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

    @Override
    public void setResult(UnionRegister register) {
        inProgress = false;
        if (overflow) {
            register.isOverflow = true;
            register.infoMessage = "UInt32 Overflow";
        } else {
            register.isOverflow = false;
            register.isNull = false;
            register.uInt32Value = value & 0x0_FFFFFFFFL;
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

    private void checkOverlong(int secondByte, UnionRegister register) {
        register.isOverlong = value == 0 && ((secondByte & SIGN_BIT_MASK) == 0);
    }
}
