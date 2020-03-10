package com.exactpro.epfast.template.simple;

public class Int32Field extends FieldInstruction implements com.exactpro.epfast.template.Int32Field {

    private FieldOperator operator;

    @Override
    public FieldOperator getOperator() {
        return operator;
    }

    public void setOperator(FieldOperator operator) {
        this.operator = operator;
    }
}
