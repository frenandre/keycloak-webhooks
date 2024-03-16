package nikohadouken.keycloak.provider;

import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

import java.util.Arrays;
import java.util.List;

import org.jboss.logging.Logger;

import nikohadouken.keycloak.webhook.WebhookClient;

public class WebhookEventListenerProviderFactory implements EventListenerProviderFactory {

    private static final Logger logger = Logger.getLogger("org.keycloak.events");

    private static final String ENV_WEBHOOKS_BASE_URL = "KEYCLOAK_WEBHOOKS_BASE_URL";
    private static final String ENV_WEBHOOKS_API_KEY = "KEYCLOAK_WEBHOOKS_API_KEY";
    private static final String ENV_WEBHOOKS_TAKEN = "KEYCLOAK_WEBHOOKS_TAKEN";

    private String baseUrl;
    private String apiKey;
    private List<String> taken;

    @Override
    public EventListenerProvider create(KeycloakSession keycloakSession) {

        WebhookClient client = new WebhookClient(baseUrl, apiKey);

        return new WebhookEventListenerProvider(client, keycloakSession, logger, taken);
    }

    @Override
    public void init(Config.Scope scope) {
        String baseUrl = System.getenv(ENV_WEBHOOKS_BASE_URL);
        if (baseUrl != null) {
            this.baseUrl = baseUrl;
        }
        String apiKey = System.getenv(ENV_WEBHOOKS_API_KEY);
        if (apiKey != null) {
            this.apiKey = apiKey;
        } else {
            this.apiKey = "";
        }
        String taken = System.getenv(ENV_WEBHOOKS_TAKEN);
        if (taken != null) {
            this.taken = Arrays.asList(taken.split(","));
        }
    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return "webhook_event_listener";
    }
}