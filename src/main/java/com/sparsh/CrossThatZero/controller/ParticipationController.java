package com.sparsh.CrossThatZero.controller;

import com.sparsh.CrossThatZero.model.Match;
import com.sparsh.CrossThatZero.model.User;
import com.sparsh.CrossThatZero.service.ParticipationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/participation")
public class ParticipationController {

    @Autowired
    public ParticipationService participationService;

//    @GetMapping("/")
//    public void createParticipation() {
//        User user = new User();
//        Match match = new Match();
//        participationService.createParticipation(user, match);
//    }
}
