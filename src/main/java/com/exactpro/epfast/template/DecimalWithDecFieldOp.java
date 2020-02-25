package com.exactpro.epfast.template;

public interface DecimalWithDecFieldOp extends FieldInstruction {

    Presence getPresence();

    FieldOperator getExponent();

    FieldOperator getMantissa();

}
