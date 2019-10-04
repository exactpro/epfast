package com.exactpro.epfast.decoder.unicode;

import com.exactpro.epfast.decoder.OverflowException;
import com.exactpro.epfast.decoder.integer.DecodeNullableUInt32;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class DecodeNullableByteVector extends DecodeByteVector {

    private DecodeNullableUInt32 lengthDecoder = new DecodeNullableUInt32();

    private Long messageLength;

    public void decode(ByteBuf buf) {
        value = new ArrayList<>();
        lengthDecoder.decode(buf);
        if (lengthDecoder.isReady()) {
            lengthReady = true;
            messageLength = lengthDecoder.getValue();
            overflow = lengthDecoder.isOverflow();
            if ((messageLength != null) && (messageLength > 0)) {
                readIndex = buf.readerIndex();
                readableBytes = buf.readableBytes() + readIndex;
                while ((readIndex < readableBytes) && !ready) {
                    if (counter < messageLength) {
                        value.add(buf.getByte(readIndex++));
                        counter++;
                    }
                    if (counter == messageLength) {
                        ready = true;
                    }
                }
                buf.readerIndex(readIndex);
            } else {
                ready = true;
            }

        }
    }

    public void continueDecode(ByteBuf buf) {
        if (lengthReady) {
            readIndex = buf.readerIndex();
            readableBytes = buf.readableBytes() + readIndex;
            while ((readIndex < readableBytes) && !ready) {
                if (counter < messageLength) {
                    value.add(buf.getByte(readIndex++));
                    counter++;
                }
                if (counter == messageLength) {
                    ready = true;
                }
            }
            buf.readerIndex(readIndex);
        } else {
            lengthDecoder.continueDecode(buf);
            if (lengthDecoder.isReady()) {
                lengthReady = true;
                messageLength = lengthDecoder.getValue();
                overflow = lengthDecoder.isOverflow();
                if (messageLength > 0) {
                    readIndex = buf.readerIndex();
                    readableBytes = buf.readableBytes() + readIndex;
                    while ((readIndex < readableBytes) && !ready) {
                        if (counter < messageLength) {
                            value.add(buf.getByte(readIndex++));
                            counter++;
                        }
                        if (counter == messageLength) {
                            ready = true;
                        }
                    }
                    buf.readerIndex(readIndex);
                }

            }
        }
    }

    public byte[] getValue() throws OverflowException {
        if (overflow) {
            throw new OverflowException("exponent value range is int32");
        } else {
            byte[] finalVal = new byte[value.size()];
            for (int i = 0; i < value.size(); i++) {
                finalVal[i] = value.get(i);
            }
            return messageLength == null ? null : finalVal;
        }
    }
}
