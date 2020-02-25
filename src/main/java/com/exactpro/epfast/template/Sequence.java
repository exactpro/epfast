package com.exactpro.epfast.template;

import java.util.List;

public interface Sequence extends FieldInstruction {

    Presence getPresence();

    IdentityRef getTypeRef();

    Length getLength();

    List<Instruction> getInstructions();

}
