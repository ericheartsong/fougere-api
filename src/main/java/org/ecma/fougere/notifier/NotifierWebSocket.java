package org.ecma.fougere.notifier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.websockets.next.*;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@WebSocket(path = "/notification")
@ApplicationScoped
public class NotifierWebSocket implements Notifier {

    private static final Logger LOG = Logger.getLogger(NotifierWebSocket.class);

    @Inject
    WebSocketConnection connection;

    @Inject
    OpenConnections openConnections;

    @Inject
    ObjectMapper objectMapper;

   @OnOpen
    public Uni<Void> onOpen() throws JsonProcessingException {
        LOG.info("=== CONNEXION WEBSOCKET OUVERTE ===");
        NotificationMessage welcomeMsg = new NotificationMessage();

        welcomeMsg.setCodeTypeMessage(NotificationMessage.codeTypeMessage.HANDSHAKE.name());
        welcomeMsg.setConnectionId(connection.id());
        String jsonMessage = objectMapper.writeValueAsString(welcomeMsg);

        // CORRECTIF PRODUCTION : On attend 100ms que Render stabilise la ligne
        // avant de pousser le premier message de Handshake
        return Uni.createFrom().item(jsonMessage)
                .chain(msg -> connection.sendText(msg));
    }

    @OnClose
    public void onClose() {

    }

    /*@OnTextMessage
    public Uni<Void> onMessage(String messageBrut) {
        LOG.infof("Message reçu: %s", messageBrut);
        try {
            JsonNode json = objectMapper.readTree(messageBrut);

            // Si le client Vite nous envoie le signal d'initialisation manuel
            if (json.has("codeTypeMessage") && "INIT".equals(json.get("codeTypeMessage").asText())) {
                NotificationMessage welcomeMsg = new NotificationMessage();
                welcomeMsg.setCodeTypeMessage(NotificationMessage.codeTypeMessage.HANDSHAKE.name());
                welcomeMsg.setConnectionId(connection.id());

                LOG.infof("Envoi du handshake, connectionId=%s", connection.id());

                // On renvoie le handshake. Le tunnel réseau étant "forcé" par le client, le flux passera
                return connection.sendText(objectMapper.writeValueAsString(welcomeMsg));
            }
        } catch (Exception e) {
            // Loggez l'erreur de parsing si nécessaire
            LOG.error("Erreur lors du traitement du message", e); // <-- le vrai fix
        }

        return Uni.createFrom().voidItem();
    }*/

    @Override
    public Uni<Void> Notifier(NotificationMessage message, String connectionId) {
        // Reactive call to await latency. Block must be done by the caller.
        return Multi.createFrom().iterable(openConnections)
                //.filter(conn -> !conn.id().equals(connectionId))
                .onItem().transformToUniAndConcatenate(conn -> {
                    try {
                        return conn.sendText(objectMapper.writeValueAsString(message));
                    } catch (JsonProcessingException e) {
                        // surtout on ne sfait rien
                        return Uni.createFrom().voidItem();
                    }
                })
                .collect().last()
                .replaceWithVoid();
    }
}


