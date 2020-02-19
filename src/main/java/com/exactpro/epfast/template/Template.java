package com.exactpro.epfast.template;

public interface Template {

    Identity getTemplateId();

    Dictionary getDictionary(); // XXX I'm not sure we need it here

    IdentityRef getTypeRef();

    Instruction getInstruction();
}
