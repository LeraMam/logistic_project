package com.valeria.demo.services;

import com.valeria.demo.db.entity.CompanyEntity;
import com.valeria.demo.db.entity.UserEntity;
import com.valeria.demo.db.entity.UserRole;
import com.valeria.demo.db.repositories.CompanyRepository;
import com.valeria.demo.db.repositories.UserRepository;
import com.valeria.demo.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
/*import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;*/
/*import org.springframework.security.crypto.password.PasswordEncoder;*/
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, CompanyRepository companyRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity getUserById(Long id){
        return userRepository.findUserEntityById(id);
    }

    public Optional<UserEntity> getUserByLogin(String login){
        return userRepository.findUserEntityByLogin(login);
    }

    /*public UserEntity loginOrRegister(UserEntity userEntity){
        UserEntity existingUser = userRepository.findUserEntityByLogin(userEntity.getLogin());
        if (existingUser != null) {
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(userEntity.getLogin(), userEntity.getPassword())
            );
            return existingUser;
        }
        else{
            UserEntity newUser = addNewUser(userEntity);
            Authentication authentication = new UsernamePasswordAuthenticationToken(newUser.getLogin(), newUser.getPassword());
            Authentication authenticated = authenticationManager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(authenticated);
            return userEntity;
        }
    }*/

    public UserEntity addNewUser(UserEntity userEntity){
        if(userRepository.findUserEntityByLogin(userEntity.getLogin()).isPresent()){
            throw new BadRequestException("Пользователь с логином " + userEntity.getLogin() + " уже существует");
        }
        if(userEntity.getRole() == UserRole.ROLE_COMPANY){
            CompanyEntity companyEntity = new CompanyEntity();
            companyRepository.save(companyEntity);
            userEntity.setCompany(companyEntity);
        }
        String encodedPassword = passwordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(encodedPassword);
        UserEntity addedEntity = userRepository.save(userEntity);
        return addedEntity;
    }
}
