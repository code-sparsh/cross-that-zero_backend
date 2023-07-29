package com.sparsh.CrossThatZero.listeners;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.sparsh.CrossThatZero.dto.*;
import com.sparsh.CrossThatZero.model.Room;
import com.sparsh.CrossThatZero.repository.RoomRepository;
import com.sparsh.CrossThatZero.service.GameValidatorService;
import com.sparsh.CrossThatZero.service.SocketService;
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

    @Override
    public void onData(SocketIOClient client, PlayerMoveDto playerMoveDto, AckRequest ackRequest) throws Exception {


        UUID sessionId = client.getSessionId();

        String roomName = SocketService.sessionIdMapRoomName.get(sessionId);
        PlayerType playerType = SocketService.sessionIdMapPlayerType.get(sessionId);
        System.out.println(playerType);


        Optional<Room> room = roomRepository.findById(UUID.fromString(roomName));

        // can't do anything because assuming it to be false always
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
        if (board[move] == '-') {
            if (playerType == PlayerType.ZERO)
                board[move] = '0';

            if (playerType == PlayerType.CROSS)
                board[move] = 'X';
        }
        room.get().setBoard(board);

        roomRepository.save(room.get());

        RoomDto roomDto = modelMapper.map(room, RoomDto.class);

        socketIOServer.getRoomOperations(roomName).sendEvent("room", roomDto);

        // if cross wins
        if (gameValidatorService.isCrossWinner(board)) {
            WinnerDto winnerDto = new WinnerDto(WinnerType.CROSS);
            socketIOServer.getRoomOperations(roomName).sendEvent("winner", winnerDto);
        }

        // if zero wins
        if (gameValidatorService.isZeroWinner(board)) {
            WinnerDto winnerDto = new WinnerDto(WinnerType.ZERO);
            socketIOServer.getRoomOperations(roomName).sendEvent("winner", winnerDto);
        }

        // if it's a TIE
        if (gameValidatorService.isGameDone(board)) {
            WinnerDto winnerDto = new WinnerDto(WinnerType.TIE);
            socketIOServer.getRoomOperations(roomName).sendEvent("winner", winnerDto);
        }

        System.out.println(playerMoveDto.getMove());
        System.out.println("hey i got an event");
    }
}
