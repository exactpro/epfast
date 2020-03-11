package com.exactpro.epfast.template.simple;

public class UInt64Field extends FieldInstruction implements com.exactpro.epfast.template.UInt64Field {

    private FieldOperator operator;

    @Override
    public FieldOperator getOperator() {
        return operator;
    }

    public void setOperator(FieldOperator operator) {
        this.operator = operator;
    }
}
