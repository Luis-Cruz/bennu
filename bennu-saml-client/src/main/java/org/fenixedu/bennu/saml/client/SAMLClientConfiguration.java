package org.fenixedu.bennu.saml.client;

import org.fenixedu.commons.configuration.ConfigurationInvocationHandler;
import org.fenixedu.commons.configuration.ConfigurationManager;
import org.fenixedu.commons.configuration.ConfigurationProperty;

public class SAMLClientConfiguration {

    @ConfigurationManager(description = "Bennu SAML Client Configuration")
    public static interface ConfigurationProperties {

        @ConfigurationProperty(key = "saml.enabled", defaultValue = "false", description = "Whether the SAML client is enabled")
        public Boolean samlEnabled();

        @ConfigurationProperty(key = "saml.serviceProviderFinalMetadataLocation", description = "The path to the final metadata(with all the possible callback/AssertionConsumerService), it probably needs to be generated by hand")
        public String serviceProviderFinalMetadataPath();
    }

    public static ConfigurationProperties getConfiguration() {
        return ConfigurationInvocationHandler.getConfiguration(ConfigurationProperties.class);
    }

}
