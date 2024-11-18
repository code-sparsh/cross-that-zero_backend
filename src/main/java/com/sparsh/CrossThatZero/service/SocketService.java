package com.sparsh.CrossThatZero.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparsh.CrossThatZero.dto.*;
import com.sparsh.CrossThatZero.listeners.PlayerMoveEventListener;
import com.sparsh.CrossThatZero.model.QueuePlayer;
import com.sparsh.CrossThatZero.model.Room;
import com.sparsh.CrossThatZero.model.RoomStatus;
import com.sparsh.CrossThatZero.redis.RedisMessagePublisher;
import com.sparsh.CrossThatZero.redis.RedisSubscriptionManager;
import com.sparsh.CrossThatZero.repository.RoomRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class SocketService {

    private final SocketIOServer socketIOServer;
    private final RedisService redisService;
    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;
    private final PlayerMoveEventListener playerMoveEventListener;
    private final RedisSubscriptionManager redisSubscriptionManager;
    private final RedisMessagePublisher redisMessagePublisher;

    public SocketService(SocketIOServer socketIOServer, RedisService redisService, RoomService roomService, RoomRepository roomRepository, ModelMapper modelMapper, PlayerMoveEventListener playerMoveEventListener, RedisSubscriptionManager redisSubscriptionManager, RedisMessagePublisher redisMessagePublisher) {
        this.socketIOServer = socketIOServer;
        this.redisService = redisService;
        this.roomRepository = roomRepository;
        this.modelMapper = modelMapper;
        this.playerMoveEventListener = playerMoveEventListener;
        this.redisSubscriptionManager = redisSubscriptionManager;
        this.redisMessagePublisher = redisMessagePublisher;
    }

    @PostConstruct
    public void init() {
        socketIOServer.addConnectListener((client) -> {
            UUID sessionId = client.getSessionId();
            String username = client.getHandshakeData().getSingleUrlParam("username");

            if (username == null) {
                client.disconnect();
                return;
            }

            QueuePlayer existingPlayer = redisService.removeFromQueue();

            if (existingPlayer == null) {
//                redisService.put(sessionId.toString(), username);
                redisService.put(username, sessionId.toString());
                Room room = new Room(null, username);
                redisService.put(sessionId.toString(), room.getId().toString());

                redisSubscriptionManager.subscribeToRoom(room.getId().toString());

                redisService.addToQueue(new QueuePlayer(sessionId, username, room));
                return;
            }

//            Room room = new Room(username, existingPlayer.getUsername());
            Room room = existingPlayer.getRoom();
            room.setCrossPlayer(username);
            room = roomRepository.save(room);

            System.out.println("existing player: " + existingPlayer.getUsername());

            System.out.println(existingPlayer.getSessionID().toString());

//            SocketIOClient existingClient = socketIOServer.getClient(existingPlayer.getSessionID());
//            if (existingClient == null) {
//                redisService.removeFromQueue();
//                redisService.addToQueue(new QueuePlayer(sessionId, username));
//                return;
//            }

            redisSubscriptionManager.subscribeToRoom(room.getId().toString());

//            existingClient.joinRoom(room.getId().toString());
//            client.joinRoom(room.getId().toString());

            RoomDto roomDto = modelMapper.map(room, RoomDto.class);
            GameStatusDto gameStatusDto = new GameStatusDto(GameStatusType.STARTED, roomDto.getCrossPlayer(), roomDto.getZeroPlayer());

            // mapping the sessionID with RoomID into redis key-value store
//            redisService.put(existingPlayer.getSessionID().toString(), room.getId().toString());
            redisService.put(sessionId.toString(), room.getId().toString());

            // mapping the sessionIDs with their usernames into redis key-value store
//            redisService.put(existingPlayer.getSessionID().toString(), existingPlayer.getUsername());
//            redisService.put(sessionId.toString(), username);
            redisService.put(username, sessionId.toString());

//            socketIOServer.getRoomOperations(room.getId().toString()).sendEvent("room", roomDto);
            redisMessagePublisher.publish(room.getId().toString(), roomDto);
            redisMessagePublisher.publish(room.getId().toString(), gameStatusDto);
        });

        socketIOServer.addEventListener("playerMove", PlayerMoveDto.class, playerMoveEventListener);

        socketIOServer.addDisconnectListener((client) -> {
            UUID sessionId = client.getSessionId();

//            String username = redisService.get(sessionId.toString());
//            String roomID = redisService.get(username);

            String roomID = redisService.get(sessionId.toString());

            System.out.println("roomID: " + roomID);

            if (roomID == null) {
                redisService.removeFromQueue();
                return;
            }

            Optional<Room> room = roomRepository.findById(UUID.fromString(roomID));

            if (room.isEmpty()) {
                redisSubscriptionManager.unsubscribeFromRoom(roomID);
                redisService.removeFromQueue();
                return;
            }

            if (!room.get().getStatus().equals(RoomStatus.PARTIALLY_COMPLETED)) {
                redisSubscriptionManager.unsubscribeFromRoom(roomID);
                redisService.removeFromQueue();
                return;
            }

            room.get().setStatus(RoomStatus.CANCELLED);
            roomRepository.save(room.get());

            GameStatusDto gameStatusDto = modelMapper.map(room.get(), GameStatusDto.class);
            gameStatusDto.setStatus(GameStatusType.ENDED);

            redisMessagePublisher.publish(roomID, gameStatusDto);
//            socketIOServer.getRoomOperations(room.get().getId().toString()).disconnect();
            redisSubscriptionManager.unsubscribeFromRoom(room.get().toString());
        });
    }

    public void broadcastRoomMessage(String message) throws JsonProcessingException {


        RoomMessageType messageType = null;

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        JsonNode jsonNode = objectMapper.readTree(message);
        String classAttribute = jsonNode.get("@class").asText();

        RoomMessageDto dto = null;


        if (classAttribute.equals(RoomDto.class.getName())) {

            dto = objectMapper.readValue(message, RoomDto.class);
            messageType = RoomMessageType.room;
            System.out.println("yes its matching with: " + messageType.toString());
        } else if (classAttribute.equals(WinnerDto.class.getName())) {
            dto = objectMapper.readValue(message, WinnerDto.class);
            messageType = RoomMessageType.winner;
        } else if (classAttribute.equals(GameStatusDto.class.getName())) {
            dto = objectMapper.readValue(message, GameStatusDto.class);
            messageType = RoomMessageType.game_status;

            GameStatusDto status = (GameStatusDto) dto;

            String crossPlayer = dto.getCrossPlayer();
            String zeroPlayer = dto.getZeroPlayer();

            String crossID = redisService.get(crossPlayer);
            String zeroID = redisService.get(zeroPlayer);

            SocketIOClient crossClient = socketIOServer.getClient(UUID.fromString(crossID));
            SocketIOClient zeroClient = socketIOServer.getClient(UUID.fromString(zeroID));

            if (status.getStatus() == GameStatusType.ENDED) {
                disconnectClient(crossClient);
                disconnectClient(zeroClient);
            }
        }


        String crossPlayer = dto.getCrossPlayer();
        String zeroPlayer = dto.getZeroPlayer();

        String crossID = redisService.get(crossPlayer);
        String zeroID = redisService.get(zeroPlayer);

        System.out.println("Username:" + crossPlayer + " ID: " + crossID);
        System.out.println("UUID: " + UUID.fromString(crossID));

        SocketIOClient crossClient = socketIOServer.getClient(UUID.fromString(crossID));
        SocketIOClient zeroClient = socketIOServer.getClient(UUID.fromString(zeroID));

        if (crossClient != null) {
            crossClient.sendEvent(messageType.toString(), dto);
        }
        if (zeroClient != null) {
            zeroClient.sendEvent(messageType.toString(), dto);
        }
    }

    public void disconnectClient(SocketIOClient client) {
        if (client != null)
            client.disconnect();
    }

}
