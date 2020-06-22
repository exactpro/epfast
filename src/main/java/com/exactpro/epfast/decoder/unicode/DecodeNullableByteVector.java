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

import com.exactpro.epfast.decoder.OverflowException;
import com.exactpro.epfast.decoder.integer.DecodeNullableUInt32;
import io.netty.buffer.ByteBuf;

public final class DecodeNullableByteVector extends DecodeByteVector {

    private DecodeNullableUInt32 lengthDecoder = new DecodeNullableUInt32();

    private Long messageLength;

    public void decode(ByteBuf buf) {
        reset();
        lengthDecoder.decode(buf);
        if (lengthDecoder.isReady()) {
            lengthReady = true;
            try {
                messageLength = lengthDecoder.getValue();
            } catch (OverflowException ex) {
                overflow = true;
            }
            if (messageLength != null && messageLength > 0) {
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

    public void continueDecode(ByteBuf buf) {
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
            lengthDecoder.continueDecode(buf);
            if (lengthDecoder.isReady()) {
                lengthReady = true;
                try {
                    messageLength = lengthDecoder.getValue();
                } catch (OverflowException ex) {
                    overflow = true;
                }
                if (messageLength != null && messageLength > 0) {
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
    }

    public byte[] getValue() throws OverflowException {
        if (overflow) {
            throw new OverflowException("length value range is uint32");
        } else {
            byte[] finalVal = new byte[value.size()];
            for (int i = 0; i < value.size(); i++) {
                finalVal[i] = value.get(i);
            }
            return messageLength == null ? null : finalVal;
        }
    }
}
