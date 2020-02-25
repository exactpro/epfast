package com.exactpro.epfast.template;

public interface DecimalField extends FieldInstruction {

    Presence getPresence();

    FieldOperator getOperator();

    DecFieldOperator getDecimalOperator();

}