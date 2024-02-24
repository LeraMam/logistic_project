package com.valeria.demo.services;

import com.valeria.demo.db.entity.CompanyEntity;
import com.valeria.demo.db.entity.UserEntity;
import com.valeria.demo.db.entity.UserRole;
import com.valeria.demo.db.repositories.CompanyRepository;
import com.valeria.demo.db.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    @Autowired
    public UserService(UserRepository userRepository, CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    public UserEntity getUserById(Long id){
        return userRepository.findUserEntityById(id);
    }

    public UserEntity addNewUser(UserEntity userEntity){
        System.out.println(userEntity);
        if(userEntity.getRole()== UserRole.COMPANY){
            CompanyEntity companyEntity = new CompanyEntity();
            companyRepository.save(companyEntity);
            userEntity.setCompany(companyEntity);
        }
        return userRepository.save(userEntity);
    }
}
