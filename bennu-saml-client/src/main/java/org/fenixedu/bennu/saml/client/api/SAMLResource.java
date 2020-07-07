
package org.fenixedu.bennu.saml.client.api;

import com.google.common.base.Strings;
import org.fenixedu.bennu.saml.client.SAMLClientConfiguration;
import org.fenixedu.bennu.core.domain.Singleton;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.UserProfile;
import org.fenixedu.bennu.core.domain.exceptions.AuthorizationException;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.portal.servlet.PortalLoginServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Path("/saml-client/login")
public class SAMLResource {

    private static final Logger logger = LoggerFactory.getLogger(SAMLResource.class);

    @GET
    @Path("/returnFromSAML")
    public Response returnFromSAML(final @QueryParam("ticket") String ticket,
                                   final @Context HttpServletRequest request,
                                   final @Context HttpServletResponse response)
            throws UnsupportedEncodingException, URISyntaxException {

        // Fail fast if SAML is not enabled
        if (!SAMLClientConfiguration.getConfiguration().samlEnabled()) {
            return Response.status(Status.NOT_FOUND).build();
        }

        // We should always have a ticket here, so fail fast if not
        if (Strings.isNullOrEmpty(ticket)) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        final String callback = null; // TODO get from parameter or request

        // Check the callback is valid
        final Optional<String> cb = decode(callback).filter(PortalLoginServlet::validateCallback);
        if (!cb.isPresent()) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        String resultLocation = cb.get();

        try {
            // Begin by logging out
            Authenticate.logout(request, response);

            // Validate the ticket
            final String requestURL = URLDecoder.decode(request.getRequestURL().toString(), "UTF-8");

            final String username = null; // TODO validate ticket and get username;
            final User user = getUser(username);
            Authenticate.login(request, response, user, "SAML Authentication");
            logger.trace("Logged in user {}, redirecting to {}", username, resultLocation);
        } catch (AuthorizationException e) {
            logger.debug(e.getMessage(), e);
            // Append the login_failed parameter to the callback
            resultLocation = resultLocation + (resultLocation.contains("?") ? "&" : "?") + "login_failed=true";
        }
        return Response.status(Status.FOUND).location(new URI(resultLocation)).build();
    }

    private static Optional<String> decode(final String base64Callback) {
        try {
            return Optional.of(new String(Base64.getUrlDecoder().decode(base64Callback), StandardCharsets.UTF_8));
        } catch (final IllegalArgumentException e) {
            // Invalid Base64, return an empty Optional
            return Optional.empty();
        }
    }

    private User getUser(final String username) {
        return Singleton.getInstance(() -> User.findByUsername(username), () -> {
            logger.info("Created new user for {}", username);
            return new User(username, new UserProfile("Unknown", "User", null, null, null));
        });
    }

}
