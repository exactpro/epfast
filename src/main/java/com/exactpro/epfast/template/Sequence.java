package com.exactpro.epfast.template;

public interface Sequence extends Instruction {

    IdentityRef getFieldId();

    Presence getPresence();

    Dictionary getDictionary();

    Length getLength();

    IdentityRef getTypeRef();

    Instruction getInstruction();

}
