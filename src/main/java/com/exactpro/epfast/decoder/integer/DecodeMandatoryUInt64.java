package com.exactpro.epfast.decoder.integer;

import com.exactpro.epfast.decoder.OverflowException;
import io.netty.buffer.ByteBuf;

import java.math.BigInteger;

public final class DecodeMandatoryUInt64 extends DecodeInteger {

    private static final long OVERFLOW_MASK = 0xFE00000000000000L;

    private byte[] bytes = new byte[8];

    private long value;

    public void decode(ByteBuf buf) {
        reset();
        value = 0;
        int readerIndex = buf.readerIndex();
        int readLimit = buf.writerIndex();
        int oneByte = buf.getByte(readerIndex++);
        accumulate(oneByte);
        if (oneByte < 0) {
            buf.readerIndex(readerIndex);
            return;
        }
        if (readerIndex < readLimit) {
            checkOverlong(buf.getByte(readerIndex)); //check second byte
            do {
                accumulate(buf.getByte(readerIndex++));
            } while (!ready && readerIndex < readLimit);
        } else {
            checkForSignExtension = true;
        }
        buf.readerIndex(readerIndex);
    }

    public void continueDecode(ByteBuf buf) {
        int readerIndex = buf.readerIndex();
        int readLimit = buf.writerIndex();
        if (checkForSignExtension) {
            checkOverlong(buf.getByte(readerIndex)); //continue checking
            checkForSignExtension = false;
        }
        do {
            accumulate(buf.getByte(readerIndex++));
        } while (!ready && readerIndex < readLimit);
    }

    public BigInteger getValue() throws OverflowException {
        if (overflow) {
            throw new OverflowException("UInt32 Overflow");
        } else {
            longToBytes(value, bytes);
            return new BigInteger(1, bytes);
        }
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
