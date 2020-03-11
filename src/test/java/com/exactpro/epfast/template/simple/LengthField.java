package com.exactpro.epfast.template.simple;

public class LengthField implements com.exactpro.epfast.template.LengthField {

    private Identity fieldId =  new Identity();

    private FieldOperator operator;

    @Override
    public Identity getFieldId() {
        return fieldId;
    }

    @Override
    public FieldOperator getOperator() {
        return operator;
    }

    public void setFieldId(Identity fieldId) {
        this.fieldId = fieldId;
    }

    public void setOperator(FieldOperator operator) {
        this.operator = operator;
    }
}
