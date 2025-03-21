package pl.redhat;

import io.quarkus.websockets.next.OnClose;
import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.OnTextMessage;
import io.quarkus.websockets.next.WebSocket;
import io.quarkus.websockets.next.WebSocketConnection;
import jakarta.inject.Inject;

@WebSocket(path = "/chat/{username}")
public class ChatWebSocketResource {

    @Inject
    WebSocketConnection connection; 

    private EventAssistant eventAssistant;

    public ChatWebSocketResource(EventAssistant eventAssistant) {
        this.eventAssistant = eventAssistant;
    }

    @OnOpen       
    public String onOpen() {
        return "Asystent: Cześć " +  connection.pathParam("username");
    }

    @OnTextMessage
    String onMessage(String message) {
        final String username = connection.pathParam("username");
        String response = username + ": " + message + "\n";
        response += "Asystent: " + eventAssistant.assistUser(message);
        return response;
    }

    @OnClose                    
    public void onClose() {
        connection.sendTextAndAwait("Asystent: Do zobaczenia " + connection.pathParam("username"));
    }
}