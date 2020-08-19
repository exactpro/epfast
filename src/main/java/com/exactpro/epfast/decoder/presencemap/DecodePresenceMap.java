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

package com.exactpro.epfast.decoder.presencemap;

import com.exactpro.epfast.decoder.IDecodeContext;
import com.exactpro.epfast.decoder.message.UnionRegister;
import io.netty.buffer.ByteBuf;

import java.util.BitSet;

public class DecodePresenceMap extends IDecodeContext {

    private BitSet value = new BitSet();

    private int lastNonZeroIndex = 0;

    private int setIndex = 0;

    private boolean ready;

    @Override
    public int decode(ByteBuf buf, UnionRegister register) {
        int readerIndex = buf.readerIndex();
        int readLimit = buf.writerIndex();
        while ((readerIndex < readLimit) && !ready) {
            accumulateValue(buf.getByte(readerIndex++));
        }
        buf.readerIndex(readerIndex);
        if (ready) {
            setResult(register);
            reset();
            return FINISHED;
        } else {
            return MORE_DATA_NEEDED;
        }
    }

    public void setResult(UnionRegister register) {
        register.isOverlong = setIndex > lastNonZeroIndex;
        register.isNull = false;
        register.presenceMap = new PresenceMap((BitSet) value.clone());
    }

    private void accumulateValue(int oneByte) {
        if (oneByte < 0) { // if stop bit is set
            oneByte &= CLEAR_STOP_BIT_MASK;
            ready = true;
        }
        for (int i = 0; i < 7; i++) {
            value.set(setIndex++, (((oneByte >> i) & 0b00000001) != 0));
        }
        if (oneByte != 0) {
            lastNonZeroIndex = setIndex;
        }
    }

    public final void reset() {
        value.clear();
        ready = false;
        setIndex = 0;
        lastNonZeroIndex = 0;
    }
}

