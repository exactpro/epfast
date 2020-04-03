package com.exactpro.epfast.template.simple;

public class UnicodeStringField extends FieldInstructionWithOperator
    implements com.exactpro.epfast.template.UnicodeStringField {

    private Identity lengthFieldId = new Identity();

    @Override
    public Identity getLengthFieldId() {
        return lengthFieldId;
    }

    public void setLengthFieldId(Identity lengthFieldId) {
        this.lengthFieldId = lengthFieldId;
    }
}
