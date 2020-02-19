package com.exactpro.epfast.template;

import java.util.List;

public interface Sequence extends Instruction {

    IdentityRef getFieldId();

    Presence getPresence();

    Dictionary getDictionary();

    IdentityRef getTypeRef();

    Length getLength();

    List<Instruction> getInstructions();

}
