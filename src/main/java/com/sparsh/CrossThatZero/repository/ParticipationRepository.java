package com.sparsh.CrossThatZero.repository;

import com.sparsh.CrossThatZero.model.Participation;
import com.sparsh.CrossThatZero.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Integer> {

    List<Participation> findByUser(User user);

}
