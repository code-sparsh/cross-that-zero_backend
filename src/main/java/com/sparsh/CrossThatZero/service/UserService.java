package com.sparsh.CrossThatZero.service;

import com.sparsh.CrossThatZero.dto.UserDto;
import com.sparsh.CrossThatZero.exception.AuthException;
import com.sparsh.CrossThatZero.model.User;
import com.sparsh.CrossThatZero.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserDto createUser(UserDto userDto) throws AuthException {
        Optional<User> storedUser = userRepository.findByEmail(userDto.getEmail());

        if (storedUser.isPresent()) {
            throw new AuthException("email", "Email already exists");
        }

        storedUser = userRepository.findByUsername(userDto.getUsername());

        if (storedUser.isPresent()) {
            throw new AuthException("username", "Username already exists");
        }

        String hashedPassword = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(hashedPassword);

        User userToSave = modelMapper.map(userDto, User.class);

        User savedUser = userRepository.save(userToSave);

        return modelMapper.map(savedUser, UserDto.class);
    }


    public UserDto login(UserDto userDto) throws AuthException {

        User storedUser = this.userRepository.findByEmail(userDto.getEmail()).orElseThrow(() -> new AuthException("email", "Email does not exist on the server"));

        if (passwordEncoder.matches(userDto.getPassword(), storedUser.getPassword())) {
            return modelMapper.map(storedUser, UserDto.class);
        } else
            throw new AuthException("password", "Incorrect password");
    }
}
