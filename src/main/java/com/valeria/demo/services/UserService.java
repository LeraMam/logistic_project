package com.valeria.demo.services;

import com.valeria.demo.db.entity.UserEntity;
import com.valeria.demo.db.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity addUser(UserEntity userEntity){
        System.out.println(userEntity);
        return userRepository.save(userEntity);
    }
}
