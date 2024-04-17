package com.valeria.demo.additional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BackpackResult {
    private List<Long> results;
    private BigInteger weight;
    private BigInteger price;
}
