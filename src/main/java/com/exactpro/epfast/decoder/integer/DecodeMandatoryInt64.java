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

public final class DecodeMandatoryInt64 extends DecodeInteger {

    private static final long POSITIVE_LIMIT = Long.MAX_VALUE >> 7;

    private static final long NEGATIVE_LIMIT = Long.MIN_VALUE >> 7;

    private long value;

    public int startDecode(ByteBuf buf, UnionRegister register) {
        reset();
        inProgress = true;
        int readerIndex = buf.readerIndex();
        int readLimit = buf.writerIndex();
        int oneByte = buf.getByte(readerIndex++);
        if ((oneByte & SIGN_BIT_MASK) == 0) {
            value = 0;
            accumulatePositive(oneByte);
            if (oneByte < 0) {
                setRegisterValue(register);
                buf.readerIndex(readerIndex);
                return 1;
            }
            if (readerIndex < readLimit) {
                checkOverlongPositive(buf.getByte(readerIndex), register); //check second byte
                do {
                    accumulatePositive(buf.getByte(readerIndex++));
                } while (!ready && readerIndex < readLimit);
            } else {
                checkForSignExtension = true;
            }
        } else {
            value = -1;
            accumulateNegative(oneByte);
            if (oneByte < 0) {
                setRegisterValue(register);
                buf.readerIndex(readerIndex);
                return 1;
            }
            if (readerIndex < readLimit) {
                checkOverlongNegative(buf.getByte(readerIndex), register); //check second byte
                do {
                    accumulateNegative(buf.getByte(readerIndex++));
                } while (!ready && readerIndex < readLimit);
            } else {
                checkForSignExtension = true;
            }
        }
        buf.readerIndex(readerIndex);
        if (ready) {
            setRegisterValue(register);
            return 1;
        } else {
            return 0;
        }
    }

    public int continueDecode(ByteBuf buf, UnionRegister register) {
        int readerIndex = buf.readerIndex();
        int readLimit = buf.writerIndex();
        if (value >= 0) {
            if (checkForSignExtension) {
                checkOverlongPositive(buf.getByte(readerIndex), register); //continue checking
                checkForSignExtension = false;
            }
            do {
                accumulatePositive(buf.getByte(readerIndex++));
            } while (!ready && readerIndex < readLimit);
        } else {
            if (checkForSignExtension) {
                checkOverlongNegative(buf.getByte(readerIndex), register); //check first and second bytes
                checkForSignExtension = false;
            }
            do {
                accumulateNegative(buf.getByte(readerIndex++));
            } while (!ready && readerIndex < readLimit);
        }
        buf.readerIndex(readerIndex);
        if (ready) {
            setRegisterValue(register);
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public void setRegisterValue(UnionRegister register) {
        inProgress = false;
        if (overflow) {
            register.isOverflow = true;
            register.infoMessage = "Int64 Overflow";
        } else {
            register.isOverflow = false;
            register.isNull = false;
            register.int64Value = value;
        }
    }

    private void accumulatePositive(int oneByte) {
        if (oneByte < 0) { // if stop bit is set
            oneByte &= CLEAR_STOP_BIT_MASK;
            ready = true;
        }
        if (value <= POSITIVE_LIMIT) {
            value = (value << 7) | oneByte;
        } else {
            overflow = true;
        }
    }

    private void accumulateNegative(int oneByte) {
        if (oneByte < 0) { // if stop bit is set
            oneByte &= CLEAR_STOP_BIT_MASK;
            ready = true;
        }
        if (value >= NEGATIVE_LIMIT) {
            value = (value << 7) | oneByte;
        } else {
            overflow = true;
        }
    }

    private void checkOverlongPositive(int secondByte, UnionRegister register) {
        register.isOverlong = value == 0 && ((secondByte & SIGN_BIT_MASK) == 0);
    }

    private void checkOverlongNegative(int secondByte, UnionRegister register) {
        register.isOverlong = value == -1 && ((secondByte & SIGN_BIT_MASK) != 0);
    }

    //TODO remove
    public boolean isReady() {
        return ready;
    }
}
