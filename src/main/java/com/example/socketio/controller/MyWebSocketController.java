package com.example.socketio.controller;

import io.socket.engineio.server.EngineIoServer;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jp.tang
 */
@Controller
@Setter(onMethod_ = @Autowired)
public class MyWebSocketController {

    private EngineIoServer engineIoServer;

    @RequestMapping(value = "/socket.io/", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS}, headers = "Connection!=Upgrade")
    public void httpHandler(HttpServletRequest request, HttpServletResponse response) throws IOException {
        engineIoServer.handleRequest(request, response);
    }
}
