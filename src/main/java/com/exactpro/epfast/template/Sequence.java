package com.exactpro.epfast.template;

import java.util.List;

public interface Sequence extends FieldInstruction {

    boolean isOptional();

    Reference getTypeRef();

    LengthField getLength();

    List<Instruction> getInstructions();

}
