package com.valeria.demo.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
public class TariffEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String startPoint;
    private String endPoint;
    private Double distance;
    private Double time;
    @Enumerated(EnumType.STRING)
    private TransportationType type;
    private Double price;
    private Double maxWeight;
}
