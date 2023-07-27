package com.sparsh.CrossThatZero.service;

import com.sparsh.CrossThatZero.model.Match;
import com.sparsh.CrossThatZero.model.Participation;
import com.sparsh.CrossThatZero.model.User;
import com.sparsh.CrossThatZero.repository.ParticipationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParticipationService {

    @Autowired
    ParticipationRepository participationRepository;

    public void createParticipation(User user, Match match) {
        Participation p = new Participation(user, match);
        participationRepository.save(p);
    }
}
