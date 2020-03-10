package com.exactpro.epfast.template.simple;

public abstract class FieldOperator implements com.exactpro.epfast.template.FieldOperator {

    private String initialValue;

    @Override
    public String getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(String initialValue) {
        this.initialValue = initialValue;
    }
}
