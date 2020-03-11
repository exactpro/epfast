package com.exactpro.epfast.template.simple;

import com.exactpro.epfast.template.Instruction;

public class TemplateRef implements com.exactpro.epfast.template.TemplateRef, Instruction {

    private Reference templateRef = new Reference();

    @Override
    public Reference getTemplateRef() {
        return templateRef;
    }

    public void setTemplateRef(Reference templateRef) {
        this.templateRef = templateRef;
    }
}
