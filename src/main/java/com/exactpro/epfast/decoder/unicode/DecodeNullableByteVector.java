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

package com.exactpro.epfast.decoder.unicode;

import com.exactpro.epfast.decoder.integer.DecodeNullableUInt32;
import com.exactpro.epfast.decoder.message.UnionRegister;
import io.netty.buffer.ByteBuf;

public final class DecodeNullableByteVector extends DecodeByteVector {

    private DecodeNullableUInt32 lengthDecoder = new DecodeNullableUInt32();

    private long messageLength;

    public int decode(ByteBuf buf, UnionRegister register) {
        reset();
        inProgress = true;
        lengthDecoder.decode(buf, register);
        if (lengthDecoder.isReady()) {
            lengthReady = true;
            if (register.isOverflow) {
                overflow = true;
            } else {
                messageLength = register.uInt32Value;
            }
            if (!register.isNull && messageLength > 0) {
                int readerIndex = buf.readerIndex();
                int readLimit = buf.writerIndex();
                while ((readerIndex < readLimit) && !ready) {
                    if (counter < messageLength) {
                        value.add(buf.getByte(readerIndex++));
                        counter++;
                    }
                    if (counter == messageLength) {
                        ready = true;
                    }
                }
                buf.readerIndex(readerIndex);
            } else {
                ready = true;
            }
        }
        if (ready) {
            setRegisterValue(register);
            return 1;
        } else {
            return 0;
        }
    }

    public int continueDecode(ByteBuf buf, UnionRegister register) {
        if (lengthReady) {
            int readerIndex = buf.readerIndex();
            int readLimit = buf.writerIndex();
            while ((readerIndex < readLimit) && !ready) {
                if (counter < messageLength) {
                    value.add(buf.getByte(readerIndex++));
                    counter++;
                }
                if (counter == messageLength) {
                    ready = true;
                }
            }
            buf.readerIndex(readerIndex);
        } else {
            lengthDecoder.continueDecode(buf, register);
            if (lengthDecoder.isReady()) {
                lengthReady = true;
                if (register.isOverflow) {
                    overflow = true;
                } else {
                    messageLength = register.uInt32Value;
                }
                if (!register.isNull && messageLength > 0) {
                    int readerIndex = buf.readerIndex();
                    int readLimit = buf.writerIndex();
                    while ((readerIndex < readLimit) && !ready) {
                        if (counter < messageLength) {
                            value.add(buf.getByte(readerIndex++));
                            counter++;
                        }
                        if (counter == messageLength) {
                            ready = true;
                        }
                    }
                    buf.readerIndex(readerIndex);
                } else {
                    ready = true;
                }
            }
        }
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
            register.errorMessage = "length value range is uint32";
        } else {
            byte[] finalVal = new byte[value.size()];
            for (int i = 0; i < value.size(); i++) {
                finalVal[i] = value.get(i);
            }
            register.isOverflow = false;
            if (register.isNull) {
                register.byteVectorValue = null;
            } else {
                register.byteVectorValue = finalVal;
            }
        }
    }
}
