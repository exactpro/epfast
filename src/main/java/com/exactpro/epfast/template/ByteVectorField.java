package com.exactpro.epfast.template;

public interface ByteVectorField extends FieldInstruction {

    Presence getPresence();

    FieldOperator getOperator();

    ByteVectorLength getByteVectorLength();

}
