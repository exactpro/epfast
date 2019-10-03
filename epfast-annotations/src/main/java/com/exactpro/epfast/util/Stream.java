package com.exactpro.epfast.util;

import java.util.stream.StreamSupport;

public class Stream {
    public static <T> java.util.stream.Stream<T> streamIterable(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}
