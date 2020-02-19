package com.exactpro.epfast.template;

public interface StringField extends Instruction {

    IdentityRef getFieldId();

    Presence getPresence();

    FieldOperator getOperator();

    Charset getCharset();

    ByteVectorLength getByteVectorLength();

}
