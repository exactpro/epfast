package com.exactpro.epfast.template;

public interface CompoundDecimalField extends FieldInstruction {

    boolean isOptional();

    FieldOperator getExponent();

    FieldOperator getMantissa();

}
