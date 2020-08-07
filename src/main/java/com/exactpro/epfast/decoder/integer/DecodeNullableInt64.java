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
        int readLimit = buf.writerIndex();
        if (!inProgress) {
            inProgress = true;
            int oneByte = buf.getByte(readerIndex++);
            if ((oneByte & SIGN_BIT_MASK) == 0) {
                positive = true;
                value = 0;
                accumulatePositive(oneByte);
                if (oneByte < 0) {
                    setResult(register);
                    buf.readerIndex(readerIndex);
                    return FINISHED;
                }
                if (readerIndex < readLimit) {
                    checkOverlongPositive(buf.getByte(readerIndex)); //check second byte
                    do {
                        accumulatePositive(buf.getByte(readerIndex++));
                    } while (!ready && readerIndex < readLimit);
                } else {
                    checkForSignExtension = true;
                }
            } else {
                positive = false;
                value = -1;
                accumulateNegative(oneByte);
                if (oneByte < 0) {
                    setResult(register);
                    buf.readerIndex(readerIndex);
                    return FINISHED;
                }
                if (readerIndex < readLimit) {
                    checkOverlongNegative(buf.getByte(readerIndex)); //check second byte
                    do {
                        accumulateNegative(buf.getByte(readerIndex++));
                    } while (!ready && readerIndex < readLimit);
                } else {
                    checkForSignExtension = true;
                }
            }
        } else {
            if (value >= 0) {
                if (checkForSignExtension) {
                    checkOverlongPositive(buf.getByte(readerIndex)); //continue checking
                    checkForSignExtension = false;
                }
                do {
                    accumulatePositive(buf.getByte(readerIndex++));
                } while (!ready && readerIndex < readLimit);
            } else {
                if (checkForSignExtension) {
                    checkOverlongNegative(buf.getByte(readerIndex)); //check first and second bytes
                    checkForSignExtension = false;
                }
                do {
                    accumulateNegative(buf.getByte(readerIndex++));
                } while (!ready && readerIndex < readLimit);
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

    @Override
    public void setResult(UnionRegister register) {
        inProgress = false;
        register.isOverlong = overlong;
        if (overflow) {
            register.isOverflow = true;
            register.infoMessage = "Int64 Overflow";
        } else {
            register.isOverflow = false;
            if (value == 0) {
                register.isNull = true;
            } else {
                register.isNull = false;
                register.int64Value = positive ? value - 1 : value;
            }
        }
        reset();
    }

    private void accumulatePositive(int oneByte) {
        if (oneByte < 0) { // if stop bit is set
            oneByte &= CLEAR_STOP_BIT_MASK;
            ready = true;
        }
        if (value < POSITIVE_LIMIT) {
            value = (value << 7) | oneByte;
        } else if (value == POSITIVE_LIMIT && oneByte == 0 && ready) {
            value = (value << 7);
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

    private void checkOverlongPositive(int secondByte) {
        overlong = value == 0 && ((secondByte & SIGN_BIT_MASK) == 0);
    }

    private void checkOverlongNegative(int secondByte) {
        overlong = value == -1 && ((secondByte & SIGN_BIT_MASK) != 0);
    }
}
