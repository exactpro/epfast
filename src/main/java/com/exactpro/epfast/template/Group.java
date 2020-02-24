package com.exactpro.epfast.template;

import java.util.List;

public interface Group extends FieldInstruction {

    Presence getPresence();

    Dictionary getDictionary();

    IdentityRef getTypeRef();

    List<Instruction> getInstructions();

}
