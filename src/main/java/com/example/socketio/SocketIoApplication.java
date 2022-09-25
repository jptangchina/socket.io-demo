package com.example.socketio;

import io.socket.engineio.server.EngineIoServer;
import io.socket.socketio.server.SocketIoServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SocketIoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocketIoApplication.class, args);
    }

    @Bean
    public EngineIoServer engineIoServer() {
        return new EngineIoServer();
    }

    @Bean
    public SocketIoServer socketIoServer(EngineIoServer engineIoServer) {
        return new SocketIoServer(engineIoServer);
    }

}
