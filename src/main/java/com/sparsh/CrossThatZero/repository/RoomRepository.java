package com.sparsh.CrossThatZero.repository;

import com.sparsh.CrossThatZero.model.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {

    @Query(value = "SELECT * FROM room WHERE player_count = 1", nativeQuery = true)
    List<Room> findRoomsByPlayerCountOne();

}
