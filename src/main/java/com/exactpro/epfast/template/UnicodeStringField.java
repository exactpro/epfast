package com.exactpro.epfast.template;

public interface UnicodeStringField extends FieldInstruction {

    boolean isOptional();

    FieldOperator getOperator();

    Identity getLengthFieldId();

}
