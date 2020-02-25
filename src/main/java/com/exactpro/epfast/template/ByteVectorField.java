package com.exactpro.epfast.template;

public interface ByteVectorField extends FieldInstruction {

    boolean isOptional();

    FieldOperator getOperator();

    Identity getLengthFieldId();

}
