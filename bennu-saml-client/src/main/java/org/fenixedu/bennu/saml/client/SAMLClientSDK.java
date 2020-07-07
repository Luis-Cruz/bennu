package org.fenixedu.bennu.saml.client;

import org.pac4j.core.client.IndirectClient;
import org.pac4j.core.context.JEEContext;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.context.WebContextFactory;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.saml.client.SAML2Client;
import org.pac4j.saml.config.SAML2Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

public class SAMLClientSDK {

    private static final SAML2Configuration CONFIG = new SAML2Configuration(
            "resource:samlKeystore.jks",
            "7eyAjEFQKQmSYtv2VEv43PpB",
            "EJsapuJMhrXxvf6zrzhyAVeP",
            "resource:idp-metadata.xml");
    static {
//        CONFIG.setForceAuth(true);
//        CONFIG.setPassive(true);
//        CONFIG.setAuthnRequestBindingType(SAMLConstants.SAML2_REDIRECT_BINDING_URI);
//        CONFIG.setResponseBindingType(SAMLConstants.SAML2_POST_BINDING_URI);
//        CONFIG.setUseNameQualifier(true);
//        CONFIG.setAttributeConsumingServiceIndex(1);
//        CONFIG.setAssertionConsumerServiceIndex(1);
//        CONFIG.setWantsAssertionsSigned(true);
//        CONFIG.setAuthnRequestSigned(true);
        CONFIG.setMaximumAuthenticationLifetime(3600);
        CONFIG.setServiceProviderEntityId("http://localhost:8080/fenix/saml-client/login/returnFromSAML?client_name=SAML2Client");
        //CONFIG.setServiceProviderMetadataPath(new File("sp-metadata.xml").getAbsolutePath());
        //CONFIG.setForceServiceProviderMetadataGeneration(true);
    }
    private static final SAML2Client client = new SAML2Client(CONFIG);
    static {
        client.setCallbackUrl("http://localhost:8080/fenix/saml-client/login/returnFromSAML?client_name=SAML2Client");
    }

    public static SAML2Client getClient() {
        return client;
    }

}
