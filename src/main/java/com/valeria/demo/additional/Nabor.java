package com.valeria.demo.additional;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Nabor {
    private List<Long> results;
    private BigInteger weight;
    private BigInteger price;
}
