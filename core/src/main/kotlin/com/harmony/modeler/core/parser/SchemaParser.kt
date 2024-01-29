package com.harmony.modeler.core.parser

import com.harmony.modeler.core.models.schema.Schema
import com.harmony.modeler.core.models.schema.SchemaFormat

interface SchemaParser {
    fun parse(schema: String): List<Schema>
}

fun SchemaParser(schemaFormat: SchemaFormat) = when (schemaFormat) {
    SchemaFormat.jsonschema -> JsonSSchemaParser()
    else -> JsonSSchemaParser()
}