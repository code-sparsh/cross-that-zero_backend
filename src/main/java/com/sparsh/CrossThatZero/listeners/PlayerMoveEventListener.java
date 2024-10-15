package com.sparsh.CrossThatZero.listeners;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.sparsh.CrossThatZero.dto.*;
import com.sparsh.CrossThatZero.model.Room;
import com.sparsh.CrossThatZero.model.RoomStatus;
import com.sparsh.CrossThatZero.redis.RedisMessagePublisher;
import com.sparsh.CrossThatZero.repository.RoomRepository;
import com.sparsh.CrossThatZero.service.GameValidatorService;
import com.sparsh.CrossThatZero.service.RedisService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class PlayerMoveEventListener implements DataListener<PlayerMoveDto> {

    @Autowired
    private SocketIOServer socketIOServer;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private GameValidatorService gameValidatorService;
    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisMessagePublisher redisMessagePublisher;

    @Override
    public void onData(SocketIOClient client, PlayerMoveDto playerMoveDto, AckRequest ackRequest) throws Exception {


        UUID sessionId = client.getSessionId();
//        String username = redisService.get(sessionId.toString());
//
//        String roomID = redisService.get(username);

        String roomID = redisService.get(sessionId.toString());

        Optional<Room> room = roomRepository.findById(UUID.fromString(roomID));

        // shouldn't be empty
        if (room.isEmpty()) {
            return;
        }

//        int[] array = Arrays.stream(matrix)
//                .flatMapToInt(Arrays::stream)
//                .toArray();

        int move = Integer.parseInt(playerMoveDto.getMove());
        char[] board = room.get().getBoard();

        // Invalid move since max blocks are 9
        if (move > 8) {
            return;
        }

        // ensuring if the index is not already taken
        if (board[move] != '-') {
            return;
        }

        if (playerMoveDto.getPlayerType() == PlayerType.ZERO)
            board[move] = '0';

        if (playerMoveDto.getPlayerType() == PlayerType.CROSS)
            board[move] = 'X';
        room.get().setBoard(board);

        Room updatedRoom = roomRepository.save(room.get());

        RoomDto roomDto = modelMapper.map(room, RoomDto.class);

//        socketIOServer.getRoomOperations(roomID).sendEvent("room", roomDto);
        redisMessagePublisher.publish(roomID, roomDto);

        WinnerType winner = null;

        // if cross wins
        if (gameValidatorService.isCrossWinner(board)) {
            winner = WinnerType.CROSS;
        }

        // if zero wins
        if (gameValidatorService.isZeroWinner(board)) {
            winner = WinnerType.ZERO;
        }

        // if it's a TIE
        if (gameValidatorService.isGameDone(board)) {
            winner = WinnerType.TIE;
        }

        if (winner != null) {
            WinnerDto winnerDto = new WinnerDto(room.get().getCrossPlayer(), room.get().getZeroPlayer(), winner);
//            socketIOServer.getRoomOperations(roomID).sendEvent("winner", winnerDto);

            redisMessagePublisher.publish(roomID, winnerDto);
            updatedRoom.setWinnerType(winner);
            updatedRoom.setStatus(RoomStatus.COMPLETED);
            roomRepository.save(updatedRoom);
        }

        System.out.println(playerMoveDto.getMove());
        System.out.println("hey i got an event");
    }
}
