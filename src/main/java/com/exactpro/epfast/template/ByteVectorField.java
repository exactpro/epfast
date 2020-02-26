package com.exactpro.epfast.template;

public interface ByteVectorField extends FieldInstruction {

    FieldOperator getOperator();

    Identity getLengthFieldId();

}
