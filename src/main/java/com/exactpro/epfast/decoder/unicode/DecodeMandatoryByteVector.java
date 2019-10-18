package com.exactpro.epfast.decoder.unicode;

import com.exactpro.epfast.decoder.OverflowException;
import com.exactpro.epfast.decoder.integer.DecodeMandatoryUInt32;
import io.netty.buffer.ByteBuf;

public final class DecodeMandatoryByteVector extends DecodeByteVector {

    private DecodeMandatoryUInt32 lengthDecoder = new DecodeMandatoryUInt32();

    private long messageLength;

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
            if (messageLength > 0) {
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
                if (messageLength > 0) {
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
            return finalVal;
        }
    }
}