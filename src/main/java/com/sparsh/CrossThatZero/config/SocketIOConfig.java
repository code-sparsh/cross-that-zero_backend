package com.sparsh.CrossThatZero.config;

import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;

@Configuration
public class SocketIOConfig {

    @Bean
    public SocketIOServer socketIOServer() throws FileNotFoundException {

        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();

        config.setHostname("0.0.0.0");
        config.setPort(9000);
        config.setOrigin("*");

//        File keyStore = new File("/home/sparsh/server.jks");
//        FileInputStream keyStoreStream;
//        if (keyStore.exists()) {
//            keyStoreStream = new FileInputStream(keyStore);
//            config.setKeyStore(keyStoreStream);
//            config.setKeyStorePassword("Internet1234");
//        }
//        InputStream stream = SocketIOConfig.class.getResourceAsStream("/server.jks");

//        config.setKeyStore(stream);

        // to prevent the error - "address already in use by some other server"
        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setReuseAddress(true);
        config.setSocketConfig(socketConfig);

        SocketIOServer server = new SocketIOServer(config);

        return server;
    }


}
