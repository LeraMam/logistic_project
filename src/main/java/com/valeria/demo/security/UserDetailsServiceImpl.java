package com.valeria.demo.security;

import com.valeria.demo.db.entity.UserEntity;
import com.valeria.demo.db.entity.UserRole;
import com.valeria.demo.db.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.Role;
import java.util.HashSet;
import java.util.Set;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername: "+login);
        UserEntity user = userRepository.findUserEntityByLogin(login).orElseThrow(() ->
                new UsernameNotFoundException("Пользователь с заданным логином не существует"));
        System.out.println(user);
        return SecurityUser.fromUser(user);
    }
}

