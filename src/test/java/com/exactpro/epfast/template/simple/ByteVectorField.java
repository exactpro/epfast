package com.exactpro.epfast.template.simple;

public class ByteVectorField extends FieldInstructionWithOperator
    implements com.exactpro.epfast.template.ByteVectorField {

    private Identity lengthFieldId = new Identity();

    @Override
    public Identity getLengthFieldId() {
        return lengthFieldId;
    }

    public void setLengthFieldId(Identity lengthFieldId) {
        this.lengthFieldId = lengthFieldId;
    }

}
