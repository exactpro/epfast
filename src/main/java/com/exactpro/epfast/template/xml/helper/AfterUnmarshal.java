package com.exactpro.epfast.template.xml.helper;

import com.exactpro.epfast.template.Identity;

public class AfterUnmarshal {

    public static abstract class IdentityBase implements Identity {

        private String name;

        protected String namespace;

        private String auxiliaryId;

        public void setName(String name) {
            this.name = name;
        }

        public void setNs(String namespace) {
            this.namespace = namespace;
        }

        public void setAuxiliaryId(String auxiliaryId) {
            this.auxiliaryId = auxiliaryId;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getAuxiliaryId() {
            return auxiliaryId;
        }
    }

    public static class ApplicationIdentity extends IdentityBase {

        public NsXmlParent parent;

        @Override
        public String getNs() {
            if (namespace != null) {
                return namespace;
            }
            if (parent != null) {
                return parent.getNs();
            }
            return "";
        }
    }

    public static class TemplateIdentity extends IdentityBase {

        public TemplateNsXmlParent parent;

        @Override
        public String getNs() {
            if (namespace != null) {
                return namespace;
            }
            if (parent != null) {
                return parent.getTemplateNs();
            }
            return "";
        }
    }
}
