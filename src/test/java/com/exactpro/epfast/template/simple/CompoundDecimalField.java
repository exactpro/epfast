package com.exactpro.epfast.template.simple;

public class CompoundDecimalField extends FieldInstruction
    implements com.exactpro.epfast.template.CompoundDecimalField {

    private FieldOperator exponent;

    private FieldOperator mantissa;

    @Override
    public FieldOperator getExponent() {
        return exponent;
    }

    @Override
    public FieldOperator getMantissa() {
        return mantissa;
    }

    public void setExponent(FieldOperator exponent) {
        this.exponent = exponent;
    }

    public void setMantissa(FieldOperator mantissa) {
        this.mantissa = mantissa;
    }
}
