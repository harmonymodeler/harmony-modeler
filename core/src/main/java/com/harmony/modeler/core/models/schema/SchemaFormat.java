package com.harmony.modeler.core.models.schema;

public enum SchemaFormat {
    json("json"),
    jsonschema("jsonschema"),
    yaml("yaml"),
    avro("avro");

    private String type;

    SchemaFormat(String type) {
        this.type = type;
    }

    public String value() {
        return this.type;
    }
}
