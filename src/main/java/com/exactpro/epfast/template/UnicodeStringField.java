package com.exactpro.epfast.template;

public interface UnicodeStringField extends FieldInstruction {

    Presence getPresence();

    FieldOperator getOperator();

    Charset getCharset();

    ByteVectorLength getByteVectorLength();

}
