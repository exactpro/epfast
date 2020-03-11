package com.exactpro.epfast.template.simple;

public class SimpleDecimalField extends FieldInstruction implements com.exactpro.epfast.template.SimpleDecimalField {

    FieldOperator operator;

    @Override
    public FieldOperator getOperator() {
        return operator;
    }

    public void setOperator(FieldOperator operator) {
        this.operator = operator;
    }
}
