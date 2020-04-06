/******************************************************************************
 * Copyright 2020 Exactpro (Exactpro Systems Limited)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.exactpro.epfast.template.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

public class AbstractFieldXml {

    private FieldOperatorXml operator;

    public FieldOperatorXml getOperator() {
        return operator;
    }

    @XmlElements({
        @XmlElement(name = "constant", type = ConstantOperatorXml.class, namespace = NamespaceProvider.XML_NAMESPACE),
        @XmlElement(name = "default", type = DefaultOperatorXml.class, namespace = NamespaceProvider.XML_NAMESPACE),
        @XmlElement(name = "copy", type = CopyOperatorXml.class, namespace = NamespaceProvider.XML_NAMESPACE),
        @XmlElement(name = "increment", type = IncrementOperatorXml.class, namespace = NamespaceProvider.XML_NAMESPACE),
        @XmlElement(name = "delta", type = DeltaOperatorXml.class, namespace = NamespaceProvider.XML_NAMESPACE),
        @XmlElement(name = "tail", type = TailOperatorXml.class, namespace = NamespaceProvider.XML_NAMESPACE)
    })
    public void setOperator(FieldOperatorXml operator) {
        this.operator = operator;
    }
}