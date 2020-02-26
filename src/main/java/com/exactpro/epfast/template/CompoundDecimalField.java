package com.exactpro.epfast.template;

public interface CompoundDecimalField extends FieldInstruction {

    FieldOperator getExponent();

    FieldOperator getMantissa();

}
