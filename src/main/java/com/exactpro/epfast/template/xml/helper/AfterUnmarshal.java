package com.exactpro.epfast.template.xml.helper;

import com.exactpro.epfast.template.Identity;

public class AfterUnmarshal {

    public abstract static class IdentityBase implements Identity {

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

        public String parentNs;

        @Override
        public String getNs() {
            if (namespace != null) {
                return namespace;
            }
            if (parentNs != null) {
                return parentNs;
            }
            return "";
        }
    }

    public static class TemplateIdentity extends IdentityBase {

        public String parentTemplateNs;

        @Override
        public String getNs() {
            if (namespace != null) {
                return namespace;
            }
            if (parentTemplateNs != null) {
                return parentTemplateNs;
            }
            return "";
        }
    }
}
