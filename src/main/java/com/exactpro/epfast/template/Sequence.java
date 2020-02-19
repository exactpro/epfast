package com.exactpro.epfast.template;

public interface Sequence extends Instruction {

    IdentityRef getFieldId();

    Presence getPresence();

    Dictionary getDictionary();

    IdentityRef getTypeRef();

    Length getLength();

    Instructions getInstructions();

}
