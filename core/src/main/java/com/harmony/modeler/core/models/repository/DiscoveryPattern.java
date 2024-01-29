package com.harmony.modeler.core.models.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscoveryPattern {
    private String name;
    private String capture;
}
