package com.harmony.modeler.core.models.repository;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchemaRepository {
    private UUID id;
    private String url;
    private String name;
    private String organization;
    private String rootPath;
    private String filter;
    private String localPath;
    private List<DiscoveryPattern> discoveryPatterns = new ArrayList<>();
}
