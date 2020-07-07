package org.fenixedu.bennu.saml.client;

import org.fenixedu.commons.configuration.ConfigurationInvocationHandler;
import org.fenixedu.commons.configuration.ConfigurationManager;
import org.fenixedu.commons.configuration.ConfigurationProperty;

public class SAMLClientConfiguration {

    @ConfigurationManager(description = "Bennu SAML Client Configuration")
    public static interface ConfigurationProperties {

        @ConfigurationProperty(key = "saml.enabled", defaultValue = "false", description = "Whether the SAML client is enabled")
        public Boolean samlEnabled();

    }

    public static ConfigurationProperties getConfiguration() {
        return ConfigurationInvocationHandler.getConfiguration(ConfigurationProperties.class);
    }

}
