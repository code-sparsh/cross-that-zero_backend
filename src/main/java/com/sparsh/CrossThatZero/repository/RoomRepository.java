package com.sparsh.CrossThatZero.repository;

import com.sparsh.CrossThatZero.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {

    @Query("SELECT r FROM Room r WHERE r.crossPlayer = :username OR r.zeroPlayer = :username")
    Room findRoomByUsername(@Param("username") String username);

}
