package com.valeria.demo.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String login;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    private String password;
    @OneToOne
    private CompanyEntity company;
    /*@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderEntity> orders;*/
}
