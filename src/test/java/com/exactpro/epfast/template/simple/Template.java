package com.exactpro.epfast.template.simple;

import com.exactpro.epfast.template.Instruction;

import java.util.ArrayList;
import java.util.List;

public class Template implements com.exactpro.epfast.template.Template {

    private Identity templateId = new Identity();

    private Reference typeRef = new Reference();

    private final List<Instruction> instructions = new ArrayList<>();

    @Override
    public Identity getTemplateId() {
        return templateId;
    }

    @Override
    public Reference getTypeRef() {
        return typeRef;
    }

    @Override
    public List<Instruction> getInstructions() {
        return instructions;
    }

    public void setTemplateId(Identity templateId) {
        this.templateId = templateId;
    }

    public void setTypeRef(Reference typeRef) {
        this.typeRef = typeRef;
    }

}
