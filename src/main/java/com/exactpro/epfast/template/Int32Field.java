package com.exactpro.epfast.template;

public interface Int32Field extends Instruction {

    IdentityRef getFieldId();

    Presence getPresence();

    FieldOperator getOperator();

}
