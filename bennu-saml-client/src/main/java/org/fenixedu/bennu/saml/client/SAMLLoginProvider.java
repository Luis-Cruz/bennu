package org.fenixedu.bennu.saml.client;

import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;
import org.fenixedu.bennu.portal.login.LoginProvider;
import org.pac4j.core.client.Client;
import org.pac4j.core.context.JEEContext;
import org.pac4j.core.exception.http.HttpAction;
import org.pac4j.core.http.adapter.JEEHttpActionAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SAMLLoginProvider implements LoginProvider {

    private final Escaper escaper = UrlEscapers.urlPathSegmentEscaper();

    @Override
    public void showLogin(final HttpServletRequest request, final HttpServletResponse response, final String callback)
            throws IOException {
        final JEEContext context = new JEEContext(request, response);
        final Client client = SAMLClientSDK.getClient();
        try {
            final HttpAction action = (HttpAction) client.getRedirectionAction(context).get();
            JEEHttpActionAdapter.INSTANCE.adapt(action, context);
        } catch (final HttpAction ex) {
            throw new Error(ex);
        }
    }

    @Override
    public String getKey() {
        return "saml";
    }

    @Override
    public String getName() {
        return "SAML";
    }

    @Override
    public boolean isEnabled() {
        return SAMLClientConfiguration.getConfiguration().samlEnabled();
    }
}
