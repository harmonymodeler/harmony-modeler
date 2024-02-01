package com.harmony.modeler.core.models.schema;

import com.harmony.modeler.core.models.repository.DiscoveryPattern;
import lombok.*;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchemaFile {
    private UUID id;
    private UUID schemaRepositoryId;
    private String filePath;
    private String qualifier;
    private SchemaFormat format;
    private String schema;
    private String url;
    private String name;
    private String domain;
    private String version;
    private String type;

    private String description;
    private Boolean additionalProperties;

    private DiscoveryPattern matchedBy;
}
