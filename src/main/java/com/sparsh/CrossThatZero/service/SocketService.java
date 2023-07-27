package com.sparsh.CrossThatZero.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.sparsh.CrossThatZero.dto.PlayerMoveDto;
import com.sparsh.CrossThatZero.listeners.PlayerMoveEventListener;
import com.sparsh.CrossThatZero.model.PlayerType;
import com.sparsh.CrossThatZero.model.Room;
import com.sparsh.CrossThatZero.repository.RoomRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SocketService {

    public static HashMap<UUID, String> sessionIdMapRoomName = new HashMap<>();

    public static HashMap<UUID, PlayerType> sessionIdMapPlayerType = new HashMap<>();

    @Autowired
    private SocketIOServer socketIOServer;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private PlayerMoveEventListener playerMoveEventListener;

    @PostConstruct
    private void init() {
        socketIOServer.addConnectListener(client -> {

            UUID sessionID = client.getSessionId();
            String userID = client.getHandshakeData().getSingleUrlParam("userID");

            List<Room> existingRoom = roomRepository.findRoomsByPlayerCountOne();
            if (existingRoom.size() != 0) {
                System.out.println("This is the room" + existingRoom.get(0).getId());
                existingRoom.get(0).setPlayerCount(2);
                existingRoom.get(0).setCrossPlayer(userID);
                roomRepository.save(existingRoom.get(0));

                String roomName = existingRoom.get(0).getId().toString();
                client.joinRoom(roomName);
                sessionIdMapRoomName.put(client.getSessionId(), roomName);
                sessionIdMapPlayerType.put(client.getSessionId(), PlayerType.CROSS);
                System.out.println(client.getSessionId() + " - has joined the room: " + roomName);
            } else {
                Room room = new Room();
                room.setZeroPlayer(userID);
                room.setPlayerCount(1);
                Room storedRoom = roomRepository.save(room);

                String roomName = storedRoom.getId().toString();
                client.joinRoom(roomName);
                sessionIdMapRoomName.put(client.getSessionId(), roomName);
                sessionIdMapPlayerType.put(client.getSessionId(), PlayerType.ZERO);
                System.out.println(client.getSessionId() + " - has joined the room: " + roomName);
            }
        });


        socketIOServer.addDisconnectListener(client -> {
            System.out.println("Client disconnected - " + client.getSessionId());

            String roomName = sessionIdMapRoomName.get(client.getSessionId());

            Collection<SocketIOClient> clients = socketIOServer.getRoomOperations(roomName).getClients();

            // disconnect the other client too, if present in the room
            for (SocketIOClient client1 : clients) {
                client1.disconnect();
            }

            // destroy the room from DB
            roomRepository.deleteById(UUID.fromString(roomName));
        });

        socketIOServer.addEventListener("playerMove", PlayerMoveDto.class, playerMoveEventListener);
    }


    public void sendMessageToClient(UUID sessionID, String event, String message) {
        socketIOServer.getClient(sessionID).sendEvent(event, message);
    }

    public void sendMessageToAllClients(String event, String message) {

        for (SocketIOClient client : socketIOServer.getAllClients()) {
            sendMessageToClient(client.getSessionId(), event, message);
        }
    }
}
