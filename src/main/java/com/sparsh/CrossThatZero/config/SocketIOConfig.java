package com.sparsh.CrossThatZero.config;

import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketIOConfig {

    @Bean
    public SocketIOServer socketIOServer() {

        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();

//        config.setHostname("*");
        config.setPort(9000);
        config.setOrigin("*");

        // to prevent the error - "address already in use by some other server"
        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setReuseAddress(true);
        config.setSocketConfig(socketConfig);

        SocketIOServer server = new SocketIOServer(config);

        return server;

    }


}
