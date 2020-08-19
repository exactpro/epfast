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

public final class DecodeMandatoryInt32 extends DecodeInteger {

    private static final int POSITIVE_LIMIT = Integer.MAX_VALUE >> 7;

    private static final int NEGATIVE_LIMIT = Integer.MIN_VALUE >> 7;

    private int value;

    @Override
    public int decode(ByteBuf buf, UnionRegister register) {
        int readerIndex = buf.readerIndex();
        int readerLimit = buf.writerIndex();
        if (inProgress) {
            if (value >= 0) {
                readerIndex = continuePositive(buf, readerIndex, readerLimit);
            } else {
                readerIndex = continueNegative(buf, readerIndex, readerLimit);
            }
        } else {
            inProgress = true;
            int oneByte = getByte(buf, readerIndex++);
            if ((oneByte & SIGN_BIT_MASK) == 0) {
                value = 0;
                accumulatePositive(oneByte);
                if (!ready && (readerIndex < readerLimit)) {
                    readerIndex = continuePositive(buf, readerIndex, readerLimit);
                }
            } else {
                value = -1;
                accumulateNegative(oneByte);
                if (!ready && (readerIndex < readerLimit)) {
                    readerIndex = continueNegative(buf, readerIndex, readerLimit);
                }
            }
        }
        buf.readerIndex(readerIndex);
        if (ready) {
            setResult(register);
            return FINISHED;
        } else {
            return MORE_DATA_NEEDED;
        }
    }

    private int continuePositive(ByteBuf buf, int readerIndex, int readerLimit) {
        int oneByte = getByte(buf, readerIndex++);
        if (checkForSignExtension) {
            checkOverlongPositive(oneByte);
            checkForSignExtension = false;
        }
        accumulatePositive(oneByte);
        while (!ready && (readerIndex < readerLimit)) {
            accumulatePositive(getByte(buf, readerIndex++));
        }
        return readerIndex;
    }

    private int continueNegative(ByteBuf buf, int readerIndex, int readerLimit) {
        int oneByte = getByte(buf, readerIndex++);
        if (checkForSignExtension) {
            checkOverlongNegative(oneByte);
            checkForSignExtension = false;
        }
        accumulateNegative(oneByte);
        while (!ready && (readerIndex < readerLimit)) {
            accumulateNegative(getByte(buf, readerIndex++));
        }
        return readerIndex;
    }

    private void accumulatePositive(int oneByte) {
        if (value <= POSITIVE_LIMIT) {
            accumulate(oneByte);
        } else {
            overflow = true;
        }
    }

    private void accumulateNegative(int oneByte) {
        if (value >= NEGATIVE_LIMIT) {
            accumulate(oneByte);
        } else {
            overflow = true;
        }
    }

    private void accumulate(int oneByte) {
        value = (value << 7) | oneByte;
    }

    private void checkOverlongPositive(int secondByte) {
        overlong = (value == 0) && ((secondByte & SIGN_BIT_MASK) == 0);
    }

    private void checkOverlongNegative(int secondByte) {
        overlong = (value == -1) && ((secondByte & SIGN_BIT_MASK) != 0);
    }

    private void setResult(UnionRegister register) {
        register.int32Value = value;
        register.isNull = false;
        register.isOverlong = overlong;
        register.isOverflow = overflow;
        register.infoMessage = "Int32 Overflow";
        reset();
    }
}
