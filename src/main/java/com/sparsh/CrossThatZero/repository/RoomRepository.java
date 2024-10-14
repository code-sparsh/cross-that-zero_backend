package com.sparsh.CrossThatZero.repository;

import com.sparsh.CrossThatZero.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {

//    @Query(value = "SELECT * FROM room WHERE player_count = 1", nativeQuery = true)
//    List<Room> findRoomsByPlayerCountOne();

    @Query(value = "SELECT * FROM room WHERE cross_player = :username OR zero_player = :username", nativeQuery = true)
    Room findRoomByUsername(@Param("username") String username);
//
//

}
