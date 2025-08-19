package pl.redhat;

import io.quarkus.websockets.next.OnClose;
import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.OnTextMessage;
import io.quarkus.websockets.next.WebSocket;
import io.quarkus.websockets.next.WebSocketConnection;
import io.smallrye.mutiny.Multi;

@WebSocket(path = "/chat/{username}")
public class ChatWebSocketResource {

    private final EventAssistant eventAssistant;

    private static final String EOM_MARKER = "[DONE]";

    public ChatWebSocketResource(EventAssistant eventAssistant) {
        this.eventAssistant = eventAssistant;
    }

    @OnOpen
    public Multi<String> onOpen(WebSocketConnection connection) {
        String browserLocale = getLocaleFromConnection(connection);
        String greeting = getGreetingMessage(browserLocale);
        return Multi.createFrom().items(greeting, EOM_MARKER);
    }

    @OnTextMessage
    Multi<String> onMessage(String message, WebSocketConnection connection) {
        Multi<String> response = eventAssistant.assistUser(message).onCompletion().continueWith(EOM_MARKER);
        return response;
    }

    @OnClose
    public void onClose() {
    }

    /**
     * Parses the Accept-Language header to determine the preferred locale.
     * The header can look like: "en-US,en;q=0.9,pl;q=0.8"
     * We'll simply take the first one for simplicity.
     */
    private String getLocaleFromConnection(WebSocketConnection connection) {
        String acceptLanguageHeader = connection.handshakeRequest().header("Accept-Language");
        if (acceptLanguageHeader != null && !acceptLanguageHeader.isEmpty()) {
            // Split by comma and take the first locale.
            // Example: "en-US" from "en-US,en;q=0.9"
            String locale = acceptLanguageHeader.split(",")[0];
            return locale.split("-")[0]; // Use only the language code, e.g., "en" from "en-US"
        }
        return "en"; // Default to English if no header is found
    }

    /**
     * A placeholder for a localization service that fetches the correct
     * greeting message based on the language.
     */
    private String getGreetingMessage(String language) {
        switch (language.toLowerCase()) {
            case "pl":
                return "Jestem Twoim Asystentem. Jak mogę Ci pomóc?";
            case "es":
                return "Soy tu Asistente. ¿En qué puedo ayudarte?";
            default:
                return "I am your Assistant. How can I help you?";
        }
    }
}