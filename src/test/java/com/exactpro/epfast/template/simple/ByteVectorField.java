package com.exactpro.epfast.template.simple;

public class ByteVectorField extends FieldInstruction implements com.exactpro.epfast.template.ByteVectorField {

    private FieldOperator operator;

    private Identity lengthFieldId = new Identity();

    @Override
    public FieldOperator getOperator() {
        return operator;
    }

    @Override
    public Identity getLengthFieldId() {
        return lengthFieldId;
    }

    public void setOperator(FieldOperator operator) {
        this.operator = operator;
    }

    public void setLengthFieldId(Identity lengthFieldId) {
        this.lengthFieldId = lengthFieldId;
    }

}
