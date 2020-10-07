
package io.helidon.examples.azure.ad.b2c;

import java.io.StringReader;
import java.util.Collections;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Form;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.HttpMethod;

/**
 * Azure Active Directory B2C Examples
 */
@Path("/azure")
@RequestScoped
public class AzureADB2CResource {

    private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());

    /**
     * The greeting message provider.
     */
    private final GreetingProvider greetingProvider;

    /**
     * Using constructor injection to get a configuration property.
     * By default this gets the value from META-INF/microprofile-config
     *
     * @param greetingConfig the configured greeting message
     */
    @Inject
    public AzureADB2CResource(GreetingProvider greetingConfig) {
        this.greetingProvider = greetingConfig;
    }

    /**
     * Return a worldly greeting message.
     *
     * @return {@link JsonObject}
     */
    @SuppressWarnings("checkstyle:designforextension")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getDefaultMessage() {
        return createResponse("Test API Endpoint");
    }
    
    /**
     * Call Azure Active Directory B2C Authenticate API to validate user details.
     * This example uses  OAuth 2.0 Resource Owner Password Credentials (ROPC)
     * @return
     */
    @Path("/ad/ropc/authenticate")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getUserProfile(
                            @FormParam("username") String username, 
                            @FormParam("password") String password,
                            @FormParam("clientid") String clientid,
                            @FormParam("tenantid") String tenantid) 
                            throws Exception {
        System.out.println("..Started getUserProfile()");

        Form userform = new Form();
        userform.param("grant_type", "password")
            .param("client_id", clientid)                                
            .param("username", username)
            .param("password", password)
            .param("scope", "user.read openid profile offline_access");

        Client client = ClientBuilder.newClient();

        String serverUrl = "https://login.microsoftonline.com/"+tenantid+"/oauth2/v2.0/token";

        WebTarget target = client.target(serverUrl);

        final Invocation.Builder invocationBuilder = target.request();
        invocationBuilder.header("Content-Type", "application/x-www-form-urlencoded");
       
        final Response response = invocationBuilder.method(
                                                    HttpMethod.POST, 
                                                    Entity.entity(userform, MediaType.APPLICATION_FORM_URLENCODED), 
                                                    Response.class);
        
        System.out.println("r.getStatus()=" + response.getStatus());
        String serverResponse = response.readEntity(String.class);

        JsonReader jsonReader = Json.createReader(new StringReader(serverResponse));
        JsonObject jsonObject = jsonReader.readObject();
        
        return jsonObject;
    }

    /**
     * 
     * @param who
     * @return
     */
    private JsonObject createResponse(String who) {
        String msg = String.format("%s %s!", greetingProvider.getMessage(), who);

        return JSON.createObjectBuilder()
                .add("message", msg)
                .build();
    }
}
