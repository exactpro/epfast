package com.exactpro.epfast.decoder.presencemap;

import java.util.BitSet;

class PresenceMap {

    private BitSet bitContent;

    PresenceMap(BitSet bitContent) {
        this.bitContent = bitContent;
    }

    public boolean getValue(int index) {
        return bitContent.get(index);
    }
}
