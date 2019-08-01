package sssii.billing.ui;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;

import sssii.billing.common.Constants;
import sssii.billing.common.PermissionsException;
import sssii.billing.common.entity.BillingEntity;
import sssii.billing.common.entity.rs.UserCredentials;
import sssii.billing.common.entity.rs.UserWithToken;
import sssii.billing.common.security.AuthenticationTokenDetails;
import sssii.billing.common.security.AuthenticationTokenParser;
import sssii.billing.common.security.AuthenticationTokenSettings;


@WebServlet(urlPatterns = "/*")
@VaadinServletConfiguration(ui = BillingUI.class, productionMode = false)
public class BillingUIServlet extends VaadinServlet {
    private static final long serialVersionUID = 1L;

    private static Logger log = LoggerFactory.getLogger(BillingUIServlet.class);

    private static Client rsClient;

    private static Properties options = new Properties();
    private static Properties version = new Properties();

    // TODO review servlet lifecycle
    static {
        rsClient = ClientBuilder.newClient();

        try {
            log.debug("loading options and version");
            options.load(BillingUIServlet.class.getResourceAsStream("/options.properties"));
            version.load(BillingUIServlet.class.getResourceAsStream("/version.properties"));
        } catch (IOException ex) {
            log.error("failed to load options and version", ex);
        }
    }

    public static Properties getVersion() {
        return version;
    }

    public static Properties getOptions() {
        return options;
    }

    public static Client getRsClient() {
        return rsClient;
    }

    public static <T> T getRestEntity(String path, Class<T> clazz, Map<String, Object> parameters)
    throws PermissionsException
    {
        verifyToken();

        String uri = options.getProperty("billing.server.rest.baseuri");
        WebTarget webTarget = rsClient.target(uri).path(path);
        if(parameters != null) {
            for(String key: parameters.keySet()){
                webTarget = webTarget.queryParam(key, parameters.get(key));
            }
        }

        log.debug("querying " + webTarget.getUri());

        Response response = webTarget.request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, VaadinSessionHelper.getTokenHeader())
                .get();

        if(response.getStatusInfo() == Status.FORBIDDEN) {
            throw new PermissionsException(response.getStatusInfo().getReasonPhrase());
        }

        // TODO status != 200? how to properly handle
        if(response.getStatusInfo() == Status.NOT_FOUND) {
            return null;
        }

        T entity = response.readEntity(clazz);
        return entity;
    }

    public static <T> T getRestEntity(String path, Class<T> clazz) throws PermissionsException {
        return getRestEntity(path, clazz, null);
    }

    // TODO is this the right place?
    public static <T> List<T> getRestEntityList(String path, Class<T> clazz, Map<String, Object> parameters) throws PermissionsException{

        verifyToken();

        String uri = options.getProperty("billing.server.rest.baseuri");
        WebTarget webTarget = rsClient.target(uri).path(path);
        if(parameters != null) {
            for(String key: parameters.keySet()){
                webTarget = webTarget.queryParam(key, parameters.get(key));
            }
        }

        log.debug("querying " + webTarget.getUri());
        Response response = webTarget
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, VaadinSessionHelper.getTokenHeader())
                .get();

        if(response.getStatusInfo() == Status.FORBIDDEN) {
            throw new PermissionsException(response.getStatusInfo().getReasonPhrase());
        }

        if(response.getStatusInfo() != Status.OK) {
            throw new PermissionsException("not OK: " + response.getStatusInfo().getReasonPhrase());
        }

        // https://stackoverflow.com/questions/29307302/dropwizard-deserializing-generic-list-from-jerseyclient

        //List<T> list = response.readEntity(new GenericType<List<T>>(){});

        GenericType<List<T>> genericType = new GenericType<>(new ParameterizedType() {
              public Type[] getActualTypeArguments() {
                return new Type[]{clazz};
              }

              public Type getRawType() {
                return List.class;
              }

              public Type getOwnerType() {
                return null;
              }
        });

        // TODO validate response codes, handle errors everywhere
        List<T> list = response.readEntity(genericType);

        log.debug(list.size() + " entities read");

        return list;
    }

    public static <T> List<T> getRestEntityList(String path, Class<T> clazz) throws PermissionsException{
        return getRestEntityList(path, clazz, null);
    }

    public static Response invokeRestGet(String path) throws PermissionsException {

        verifyToken();

        String uri = options.getProperty("billing.server.rest.baseuri");
        Response response = rsClient.target(uri).path(path)
                .request()
                .header(HttpHeaders.AUTHORIZATION, VaadinSessionHelper.getTokenHeader())
                .get();

        log.debug("answer: " + response.toString());

        if(response.getStatusInfo() == Status.FORBIDDEN) {
            throw new PermissionsException(response.getStatusInfo().getReasonPhrase());
        }

        return response;
    }

    @Override
    public void init() throws ServletException {
        super.init();
        log.debug("servlet init");
    }

    public static String getBaseRestUri() {
        return options.getProperty("billing.server.rest.baseuri");
    }

    public static void updateRestEntity(String path, BillingEntity entity) throws PermissionsException {

        verifyToken();

        String uri = options.getProperty("billing.server.rest.baseuri");
        Response response = rsClient.target(uri)
                .path(path)
                .path(entity.getId().toString())
                //.request().put(Entity.entity(entity, MediaType.APPLICATION_JSON + ";charset=utf-8"));
                .request()
                .header(HttpHeaders.AUTHORIZATION, VaadinSessionHelper.getTokenHeader())
                .put(Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name())));

        log.debug("answer: " + response.toString());

        if(response.getStatusInfo() == Status.FORBIDDEN) {
            throw new PermissionsException(response.getStatusInfo().getReasonPhrase());
        }
    }

    public static void putRestEntity(String path, Object entity) throws PermissionsException {

        verifyToken();

        String uri = options.getProperty("billing.server.rest.baseuri");

        Response response = rsClient.target(uri)
                .path(path)
                .request()
                .header(HttpHeaders.AUTHORIZATION, VaadinSessionHelper.getTokenHeader())
                .put(Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name())));

        log.debug("answer: " + response.toString());

        if(response.getStatusInfo() == Status.FORBIDDEN) {
            throw new PermissionsException(response.getStatusInfo().getReasonPhrase());
        }
    }


    public static void deleteRestEntity(String path, BillingEntity entity) throws PermissionsException {
        verifyToken();

        String uri = options.getProperty("billing.server.rest.baseuri");

//        BillingEntity e = entity;
//        Integer i = entity.getId();

        Response response = rsClient.target(uri)
                .path(path).path(entity.getId().toString())
                .request()
                .header(HttpHeaders.AUTHORIZATION, VaadinSessionHelper.getTokenHeader())
                .delete();

        log.debug("answer: " + response.toString());

        if(response.getStatusInfo() == Status.FORBIDDEN) {
            throw new PermissionsException(response.getStatusInfo().getReasonPhrase());
        }

    }

    public static <T> T createRestEntity(String path, Class<T> clazz, BillingEntity entity) throws PermissionsException {
        verifyToken();

        String uri = options.getProperty("billing.server.rest.baseuri");
        entity.setId(null);

        Response response = rsClient.target(uri)
                .path(path)
                .request()
                .header(HttpHeaders.AUTHORIZATION, VaadinSessionHelper.getTokenHeader())
                .post(Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name())));

        log.debug("answer: " + response.toString());

        if(response.getStatusInfo() == Status.FORBIDDEN) {
            throw new PermissionsException(response.getStatusInfo().getReasonPhrase());
        }


        T newEntity = response.readEntity(clazz);
        return newEntity;
    }

    public static Integer createRestEntity(String path, BillingEntity entity) throws PermissionsException {
        verifyToken();

        String uri = options.getProperty("billing.server.rest.baseuri");
        entity.setId(null);

        Response response = rsClient.target(uri)
                .path(path)
                .request()
                .header(HttpHeaders.AUTHORIZATION, VaadinSessionHelper.getTokenHeader())
                .post(Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name())));

        if(response.getStatusInfo() == Status.FORBIDDEN) {
            throw new PermissionsException(response.getStatusInfo().getReasonPhrase());
        }


        Integer newId = null;

        MultivaluedMap<String, Object> headers = response.getHeaders();
        for(String key: headers.keySet()) {
            List<Object> h = headers.get(key);
            for(Object o: h) {
                //log.debug("- " + key + ": " + o.toString());

                if(key.equals(HttpHeaders.LOCATION) && response.getStatusInfo() == Status.CREATED) {
                    String s = o.toString().substring(o.toString().lastIndexOf("/") + 1);
                    newId = Integer.parseInt(s);
                }
            }
        }

        log.debug("answer: " + response.toString());
        return newId;
    }

    public static AuthenticationTokenSettings getAuthTokenSettings() {
        AuthenticationTokenSettings settings= new AuthenticationTokenSettings();

        settings.setAudience("audience");
        settings.setAuthoritiesClaimName("authoritiesClaimName");
        settings.setClockSkew(10l);
        settings.setIssuer("issuer");
        settings.setRefreshCountClaimName("refreshCountClaimName");
        settings.setRefreshLimitClaimName("refreshLimitClaimName");
        settings.setSecret("secret");

        return settings;
    }


    public static boolean login(String path, UserCredentials cred)  {
        String uri = options.getProperty("billing.server.rest.baseuri");

        Response response = rsClient.target(uri)
                .path(path)
                .request().post(Entity.entity(cred, MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name())));

        log.debug("answer: " + response.toString());

        if(response.getStatusInfo() == Status.FORBIDDEN) {
            //throw new PermissionsException(response.getStatusInfo().getReasonPhrase());
            log.debug("forbidden: " + response.getStatusInfo().getReasonPhrase());
            return false;
        }
        else if(response.getStatusInfo() == Status.OK) {
//            String token = response.readEntity(String.class);
//            log.debug("token: " + token);

            UserWithToken uwt = response.readEntity(UserWithToken.class);
            log.debug("token: " + uwt.getToken());
            for(String role: uwt.getUser().getAuthorities()) {
                log.debug("role: " + role);
            }

            AuthenticationTokenParser tokenParser = new AuthenticationTokenParser(getAuthTokenSettings());
            AuthenticationTokenDetails tokenDetails = tokenParser.parseToken(uwt.getToken());
            log.debug("token expiration: " + tokenDetails.getExpirationDate());

            VaadinSessionHelper.putTokenDetails(tokenDetails);
            VaadinSessionHelper.putUser(uwt.getUser());
            VaadinSessionHelper.setString(Constants.SESSION_TOKEN, uwt.getToken());

            return true;
        }
        else {
            log.error("Login failed: " + response.getStatusInfo().getReasonPhrase());
            return false;
        }
    }

    public static boolean loginByToken(String path, String token) {
        String uri = options.getProperty("billing.server.rest.baseuri");

        Response response = rsClient.target(uri)
                .path(path)
                .request().post(Entity.entity(token, MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name())));

        log.debug("answer: " + response.toString());

        if(response.getStatusInfo() == Status.FORBIDDEN) {
            //throw new PermissionsException(response.getStatusInfo().getReasonPhrase());
            log.debug("forbidden: " + response.getStatusInfo().getReasonPhrase());
            return false;
        }
        else if(response.getStatusInfo() == Status.OK) {
//            String token = response.readEntity(String.class);
//            log.debug("token: " + token);

            UserWithToken uwt = response.readEntity(UserWithToken.class);
            log.debug("token: " + uwt.getToken());
            for(String role: uwt.getUser().getAuthorities()) {
                log.debug("role: " + role);
            }

            AuthenticationTokenParser tokenParser = new AuthenticationTokenParser(getAuthTokenSettings());
            AuthenticationTokenDetails tokenDetails = tokenParser.parseToken(uwt.getToken());
            log.debug("token expiration: " + tokenDetails.getExpirationDate());

            VaadinSessionHelper.putTokenDetails(tokenDetails);
            VaadinSessionHelper.putUser(uwt.getUser());
            VaadinSessionHelper.setString(Constants.SESSION_TOKEN, uwt.getToken());

            return true;
        }
        else {
            log.error("Login failed: " + response.getStatusInfo().getReasonPhrase());
            return false;
        }

    }

    private static void verifyToken() throws PermissionsException {
        AuthenticationTokenDetails tokenDetails = VaadinSessionHelper.getTokenDetails();
        ZonedDateTime now = ZonedDateTime.now();

        // TODO hardcoded 1 day
        if(tokenDetails.getExpirationDate().minus(1, ChronoUnit.DAYS).compareTo(now) < 0) {
            log.debug("less than 1 day to token expire date: " + tokenDetails.getExpirationDate());
            log.debug("now: " + now);

            String uri = options.getProperty("billing.server.rest.baseuri");

            Response response = rsClient.target(uri)
                    .path("auth/renew")
                    .request()
                    .header(HttpHeaders.AUTHORIZATION, VaadinSessionHelper.getTokenHeader())
                    .post(Entity.entity(VaadinSessionHelper.getString(Constants.SESSION_TOKEN), MediaType.APPLICATION_JSON_TYPE.withCharset(StandardCharsets.UTF_8.name())));

            log.debug("answer: " + response.toString());


            if(response.getStatusInfo() == Status.FORBIDDEN) {
                log.debug("forbidden: " + response.getStatusInfo().getReasonPhrase());
                //throw new PermissionsException(response.getStatusInfo().getReasonPhrase());
            }
            else if(response.getStatusInfo() == Status.OK) {

                UserWithToken uwt = response.readEntity(UserWithToken.class);
                log.debug("token: " + uwt.getToken());
                for(String role: uwt.getUser().getAuthorities()) {
                    log.debug("role: " + role);
                }

                AuthenticationTokenParser tokenParser = new AuthenticationTokenParser(getAuthTokenSettings());
                AuthenticationTokenDetails newTokenDetails = tokenParser.parseToken(uwt.getToken());
                log.debug("token expiration: " + newTokenDetails.getExpirationDate());

                VaadinSessionHelper.putTokenDetails(newTokenDetails);
                VaadinSessionHelper.putUser(uwt.getUser());
                VaadinSessionHelper.setString(Constants.SESSION_TOKEN, uwt.getToken());
            }
            else {
                log.error("token renewal failed: " + response.getStatusInfo().getReasonPhrase());
                //return false;
            }

        }
    }
}
