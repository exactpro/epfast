package com.exactpro.epfast.template.simple;

public class AsciiStringField extends FieldInstruction implements com.exactpro.epfast.template.AsciiStringField {

    private FieldOperator operator;

    @Override
    public FieldOperator getOperator() {
        return operator;
    }

    public void setOperator(FieldOperator operator) {
        this.operator = operator;
    }
}
