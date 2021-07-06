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

public final class DecodeNullableInt64 extends DecodeInteger {

    private static final long POSITIVE_LIMIT = 0x01000000_00000000L;

    private static final long NEGATIVE_LIMIT = Long.MIN_VALUE >> 7;

    private boolean positive;

    private long value;

    @Override
    public int decode(ByteBuf buf, UnionRegister register) {
        int readerIndex = buf.readerIndex();
        int readerLimit = buf.writerIndex();
        if (bytesRead == 0) {
            int oneByte = getByte(buf, readerIndex++);
            if ((oneByte & SIGN_BIT_MASK) == 0) {
                positive = true;
                value = 0;
                accumulatePositive(oneByte);
                if (!ready && (readerIndex < readerLimit)) {
                    readerIndex = continuePositive(buf, readerIndex, readerLimit);
                }
            } else {
                positive = false;
                value = -1;
                accumulateNegative(oneByte);
                if (!ready && (readerIndex < readerLimit)) {
                    readerIndex = continueNegative(buf, readerIndex, readerLimit);
                }
            }
        } else {
            if (value >= 0) {
                readerIndex = continuePositive(buf, readerIndex, readerLimit);
            } else {
                readerIndex = continueNegative(buf, readerIndex, readerLimit);
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
        do {
            int oneByte = getByte(buf, readerIndex++);
            if (bytesRead == 2) {
                checkOverlongPositive(oneByte);
            }
            accumulatePositive(oneByte);
        } while (!ready && (readerIndex < readerLimit));
        return readerIndex;
    }

    private int continueNegative(ByteBuf buf, int readerIndex, int readerLimit) {
        do {
            int oneByte = getByte(buf, readerIndex++);
            if (bytesRead == 2) {
                checkOverlongNegative(oneByte);
            }
            accumulateNegative(oneByte);
        } while ((!ready && (readerIndex < readerLimit)));
        return readerIndex;
    }

    private void accumulatePositive(int oneByte) {
        if (value < POSITIVE_LIMIT) {
            accumulate(oneByte);
        } else if (value == POSITIVE_LIMIT && oneByte == 0 && ready) {
            value = (value << 7);
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

    private void setResult(UnionRegister register) {
        register.int64Value = positive ? value - 1 : value;
        register.isNull = value == 0;
        register.isOverlong = overlong;
        register.isOverflow = overflow;
        register.infoMessage = "Int64 Overflow";
        reset();
    }

    private void checkOverlongPositive(int secondByte) {
        overlong = value == 0 && ((secondByte & SIGN_BIT_MASK) == 0);
    }

    private void checkOverlongNegative(int secondByte) {
        overlong = value == -1 && ((secondByte & SIGN_BIT_MASK) != 0);
    }
}
