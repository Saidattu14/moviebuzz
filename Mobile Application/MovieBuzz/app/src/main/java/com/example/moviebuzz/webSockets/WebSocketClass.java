package com.example.moviebuzz.webSockets;

import okhttp3.WebSocket;

public class WebSocketClass {
    WebSocket webSocket;
    WebSocketEcho webSocketEcho;

    public WebSocketClass(WebSocket webSocket, WebSocketEcho webSocketEcho) {
        this.webSocket = webSocket;
        this.webSocketEcho = webSocketEcho;
    }

    public WebSocket getWebSocket() {
        return this.webSocket;
    }

    public WebSocketEcho getWebSocketEcho() {
        return this.webSocketEcho;
    }
}
