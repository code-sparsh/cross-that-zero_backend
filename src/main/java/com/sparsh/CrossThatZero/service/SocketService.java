package com.sparsh.CrossThatZero.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.sparsh.CrossThatZero.dto.PlayerMoveDto;
import com.sparsh.CrossThatZero.dto.RoomDto;
import com.sparsh.CrossThatZero.listeners.PlayerMoveEventListener;
import com.sparsh.CrossThatZero.model.QueuePlayer;
import com.sparsh.CrossThatZero.model.Room;
import com.sparsh.CrossThatZero.model.RoomStatus;
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

    public SocketService(SocketIOServer socketIOServer, RedisService redisService, RoomService roomService, RoomRepository roomRepository, ModelMapper modelMapper, PlayerMoveEventListener playerMoveEventListener) {
        this.socketIOServer = socketIOServer;
        this.redisService = redisService;
        this.roomRepository = roomRepository;
        this.modelMapper = modelMapper;
        this.playerMoveEventListener = playerMoveEventListener;
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
                redisService.addToQueue(new QueuePlayer(sessionId, username));
                return;
            }

            Room room = new Room(username, existingPlayer.getUsername());
            room = roomRepository.save(room);

            System.out.println("existing player: " + existingPlayer.getUsername());

            System.out.println(existingPlayer.getSessionID().toString());

            SocketIOClient existingClient = socketIOServer.getClient(existingPlayer.getSessionID());
            if (existingClient == null) {
                redisService.removeFromQueue();
                redisService.addToQueue(new QueuePlayer(sessionId, username));
                return;
            }
            existingClient.joinRoom(room.getId().toString());
            client.joinRoom(room.getId().toString());

            RoomDto roomDto = modelMapper.map(room, RoomDto.class);
            socketIOServer.getRoomOperations(room.getId().toString()).sendEvent("room", roomDto);

            // mapping the usernames with RoomID into redis key-value store
            redisService.put(existingPlayer.getUsername(), room.getId().toString());
            redisService.put(username, room.getId().toString());

            // mapping the sessionIDs with their usernames into redis key-value store
            redisService.put(existingPlayer.getSessionID().toString(), existingPlayer.getUsername());
            redisService.put(sessionId.toString(), username);
        });

        socketIOServer.addEventListener("playerMove", PlayerMoveDto.class, playerMoveEventListener);

        socketIOServer.addDisconnectListener((client) -> {
            UUID sessionId = client.getSessionId();

            String username = redisService.get(sessionId.toString());
            String roomID = redisService.get(username);

            Optional<Room> room = roomRepository.findById(UUID.fromString(roomID));

            if (!room.get().getStatus().equals(RoomStatus.PARTIALLY_COMPLETED))
                return;

            if (room.isEmpty()) {
                redisService.removeFromQueue();
                return;
            }


            room.get().setStatus(RoomStatus.CANCELLED);
            roomRepository.save(room.get());
            socketIOServer.getRoomOperations(room.get().getId().toString()).disconnect();

        });
    }


}
