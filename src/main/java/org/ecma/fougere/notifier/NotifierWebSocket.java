package org.ecma.fougere.notifier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.websockets.next.*;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@WebSocket(path = "/notification")
@ApplicationScoped
public class NotifierWebSocket implements Notifier {

    @Inject
    WebSocketConnection connection;

    @Inject
    OpenConnections openConnections;

    @Inject
    ObjectMapper objectMapper;

    @OnOpen
    public Uni<Void> onOpen() throws JsonProcessingException {
        NotificationMessage welcomeMsg = new NotificationMessage();

        welcomeMsg.setCodeTypeMessage(NotificationMessage.codeTypeMessage.HANDSHAKE.name());
        welcomeMsg.setConnectionId(connection.id());
        return connection.sendText(objectMapper.writeValueAsString(welcomeMsg));
    }

    @OnClose
    public void onClose() {

    }

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


