package com.exactpro.epfast.template.simple;

public class Int64Field extends FieldInstruction implements com.exactpro.epfast.template.Int64Field {

    private FieldOperator operator;

    @Override
    public FieldOperator getOperator() {
        return operator;
    }

    public void setOperator(FieldOperator operator) {
        this.operator = operator;
    }
}
