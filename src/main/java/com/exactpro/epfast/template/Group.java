package com.exactpro.epfast.template;

public interface Group extends Instruction {

    IdentityRef getFieldId();

    Presence getPresence();

    Dictionary getDictionary();

    IdentityRef getTypeRef();

    Instruction getInstruction();

}
