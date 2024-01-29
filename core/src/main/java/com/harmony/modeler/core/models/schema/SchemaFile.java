package com.harmony.modeler.core.models.schema;

import lombok.*;

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
}
