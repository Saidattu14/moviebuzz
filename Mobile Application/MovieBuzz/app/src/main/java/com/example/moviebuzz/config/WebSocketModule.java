package com.example.moviebuzz.config;

import com.example.moviebuzz.webSockets.WebSocketClass;
import com.example.moviebuzz.webSockets.WebSocketEcho;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

@Module
@InstallIn(SingletonComponent.class)
public class WebSocketModule {

    WebSocket webSocket;
    WebSocketListener webSocketListener;

    @Provides
    @Singleton
    WebSocketClass setUpWebSocket() {
            try {
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .readTimeout(0, TimeUnit.MILLISECONDS)
                        .build();
                Request request = new Request.Builder()
                        .url("ws://192.168.43.99:8005/")
                        .header("Sec-WebSocket-Protocol","echo-protocol")
                        .build();
                WebSocketEcho webSocketEcho = new WebSocketEcho();
                webSocket = okHttpClient.newWebSocket(request,webSocketEcho);
                okHttpClient.dispatcher().executorService().shutdown();
                return new WebSocketClass(webSocket,webSocketEcho);
            }
            catch (Exception e)
            {
                System.out.println(e);
            }
           return null;
    }
}
