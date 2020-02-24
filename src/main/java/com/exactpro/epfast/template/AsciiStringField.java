package com.exactpro.epfast.template;

public interface AsciiStringField extends FieldInstruction {

    Presence getPresence();

    FieldOperator getOperator();

    Charset getCharset();
}
