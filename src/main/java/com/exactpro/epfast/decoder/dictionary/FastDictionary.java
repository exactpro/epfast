package com.exactpro.epfast.decoder.dictionary;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

final class FastDictionary {

    private Map<String, Optional<Object>> dictionary = new HashMap<>();

    void addEntry(String key, Object entry) {
        if (entry != null) {
            dictionary.put(key, Optional.of(entry));
        } else {
            dictionary.put(key, Optional.empty());
        }
    }

    Object getEntry(String key) {
        return dictionary.get(key);
    }
}

