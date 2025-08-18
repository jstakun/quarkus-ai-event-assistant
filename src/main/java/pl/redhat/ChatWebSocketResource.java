package pl.redhat;

import io.quarkus.websockets.next.OnClose;
import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.OnTextMessage;
import io.quarkus.websockets.next.WebSocket;
import io.quarkus.websockets.next.WebSocketConnection;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;

@WebSocket(path = "/chat/{username}")
public class ChatWebSocketResource {

    @Inject
    WebSocketConnection connection; 

    private EventAssistant eventAssistant;

    private static final String EOM_MARKER = "[DONE]";

    public ChatWebSocketResource(EventAssistant eventAssistant) {
        this.eventAssistant = eventAssistant;
    }

    @OnOpen       
    public Multi<String> onOpen() {
        return Multi.createFrom().items("Jestem Twoim Asystentem. Jak mogę Ci pomóc?", EOM_MARKER);
    }

    @OnTextMessage
    Multi<String> onMessage(String message) {
        //final String username = connection.pathParam("username");
        Multi<String> response = eventAssistant.assistUser(message).onCompletion().continueWith(EOM_MARKER);
        //return Multi.createFrom().items(username + ": " + message + "\n","Asystent: ", eventAssistant.assistUser(message));
        return response;
    }

    @OnClose                    
    public void onClose() {
        //connection.sendTextAndAwait("Asystent: Do zobaczenia " + connection.pathParam("username"));
    }
}