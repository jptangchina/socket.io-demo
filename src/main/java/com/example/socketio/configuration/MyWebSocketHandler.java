package com.example.socketio.configuration;

import io.socket.engineio.server.EngineIoServer;
import io.socket.engineio.server.EngineIoWebSocket;
import io.socket.engineio.server.utils.ParseQS;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author jp.tang
 */
@Component
@Setter(onMethod_ = @Autowired)
public class MyWebSocketHandler implements WebSocketHandler {

    public static final String ATTRIBUTE_ENGINEIO_BRIDGE = "engineIo.bridge";
    public static final String ATTRIBUTE_ENGINEIO_QUERY = "engineIo.query";
    public static final String ATTRIBUTE_ENGINEIO_HEADERS = "engineIo.headers";

    private EngineIoServer engineIoServer;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        MyEngineIoWebSocket socket = new MyEngineIoWebSocket(session);
        session.getAttributes().put(ATTRIBUTE_ENGINEIO_BRIDGE, socket);
        engineIoServer.handleWebSocket(socket);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        ((MyEngineIoWebSocket) session.getAttributes().get(ATTRIBUTE_ENGINEIO_BRIDGE)).handleMessage(message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        ((MyEngineIoWebSocket) session.getAttributes().get(ATTRIBUTE_ENGINEIO_BRIDGE)).handleTransportError(exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        ((MyEngineIoWebSocket) session.getAttributes().get(ATTRIBUTE_ENGINEIO_BRIDGE)).afterConnectionClosed();
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    @SuppressWarnings("unchecked")
    static final class MyEngineIoWebSocket extends EngineIoWebSocket {

        private final WebSocketSession session;

        private final Map<String, String> query;

        private final Map<String, List<String>> headers;

        public MyEngineIoWebSocket(WebSocketSession session) {
            this.session = session;
            String query = session.getAttributes().get(ATTRIBUTE_ENGINEIO_QUERY).toString();
            if (query == null) {
                this.query = Collections.emptyMap();
            } else {
                this.query = ParseQS.decode(query);
            }
            this.headers = (Map<String, List<String>>) session.getAttributes().get(ATTRIBUTE_ENGINEIO_HEADERS);
        }

        @Override
        public Map<String, String> getQuery() {
            return this.query;
        }

        @Override
        public Map<String, List<String>> getConnectionHeaders() {
            return this.headers;
        }

        @Override
        public void write(String message) throws IOException {
            session.sendMessage(new TextMessage(message));
        }

        @Override
        public void write(byte[] message) throws IOException {
            session.sendMessage(new BinaryMessage(message));
        }

        @Override
        public void close() {
            try {
                session.close();
            } catch (Exception e) {
                // do nothing
            }
        }

        void afterConnectionClosed() {
            emit("close");
        }

        void handleMessage(WebSocketMessage<?> message) {
            if (message.getPayload() instanceof String || message.getPayload() instanceof byte[]) {
                emit("message", message.getPayload());
            }
        }

        void handleTransportError(Throwable exception) {
            emit("error", "write error", exception.getMessage());
        }
    }
}
