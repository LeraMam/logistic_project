package com.valeria.demo.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntervalWayEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<TariffEntity> tariffs;
    private Double sumDistance;
    private Double sumTime;
    private Double sumPrice;
    private Double maxWeight;
    private boolean sortByTime;
    private boolean sortByPrice;
}
