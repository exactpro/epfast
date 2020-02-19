package com.exactpro.epfast.template;

public interface DecimalField extends Instruction {

    IdentityRef getFieldId();

    Presence getPresence();

    FieldOperator getOperator();

    DecFieldOperator getDecimalOperator();

}
