package com.harmony.modeler.core.models.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
@With
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Schema {
    private String id;
    private String url;
    private String namespace;
    private String name;
    private String description;

    private SchemaFile schemaFile;
    private String schema;

    private String domain;
    private String version;
    private String type;
    private String format;
    private List<String> enumeration = new ArrayList<>();
    private List<Schema> fields = new ArrayList<>();
    private List<Schema> oneOf = new ArrayList<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schema schema = (Schema) o;
        return Objects.equals(version, schema.version)
                && Objects.equals(type, schema.type)
                && Objects.equals(fields, schema.fields)
                && Objects.equals(enumeration, schema.enumeration)
                && Objects.equals(oneOf, schema.oneOf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, format);
    }
}
