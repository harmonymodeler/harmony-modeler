package com.harmony.modeler.core.models.schema;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    /**
     * Json string representation of the original raw json but without the 'description' attribute
     * Rationale is being able to compare two schema without having to map all the check information not
     * that useful for indexing.
     */
    private String undescribedSchemaString;

    private String domain;
    private String version;
    private String type;

    private List<Schema> items;

    private String format;
    private String formatVersion;
    private List<String> enumeration = new ArrayList<>();
    private List<Schema> fields = new ArrayList<>();

    private List<Schema> possibleTypes = new ArrayList<>();

    private List<Exception> errors = new ArrayList<>();



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schema schema = (Schema) o;
        return Objects.equals(version, schema.version)
                && Objects.equals(type, schema.type)
                && Objects.equals(fields, schema.fields)
                && Objects.equals(enumeration, schema.enumeration)
                && Objects.equals(possibleTypes, schema.possibleTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, format, undescribedSchemaString, fields, possibleTypes);
    }

    @Override
    public String toString() {
        return "Schema{" +
            "name='" + name + '\'' +
            ", schema='" + undescribedSchemaString + '\'' +
            '}';
    }
}
