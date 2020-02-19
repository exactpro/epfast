package com.exactpro.epfast.template;

public interface ByteVectorField extends Instruction {

    IdentityRef getFieldId();

    Presence getPresence();

    FieldOperator getOperator();

    ByteVectorLength getByteVectorLength();

}
