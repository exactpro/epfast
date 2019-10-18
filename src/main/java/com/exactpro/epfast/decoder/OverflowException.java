package com.exactpro.epfast.decoder;

import java.io.IOException;

public class OverflowException extends IOException {
    public OverflowException(String errorMessage) {
        super(errorMessage);
    }
}
