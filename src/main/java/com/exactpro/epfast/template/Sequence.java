package com.exactpro.epfast.template;

import java.util.List;

public interface Sequence extends FieldInstruction {

    Reference getTypeRef();

    LengthField getLength();

    Dictionary getDictionary();

    List<? extends Instruction> getInstructions();

}
