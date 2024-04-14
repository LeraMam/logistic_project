package com.valeria.demo.additional;

import com.valeria.demo.db.entity.TariffEntity;
import com.valeria.demo.db.entity.TransportationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchClass {
    private TransportationType type;
    private String startPoint;
    private String endPoint;
    private String intervalPoint;
    private Double maxWeight;
    private Double sumTime;
    private Double sumPrice;
    private boolean sortByTime;
    private boolean sortByPrice;
}
