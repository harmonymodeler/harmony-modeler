package com.harmony.modeler.core.parser

import com.harmony.modeler.core.models.schema.Schema
import com.harmony.modeler.core.models.schema.SchemaFile
import com.harmony.modeler.core.models.schema.SchemaFormat
import java.nio.file.Path

interface SchemaParser {
    fun parse(schema: String): List<Schema>

    fun parse(basePath: Path,  schemaFile: SchemaFile): List<Schema>
}

fun SchemaParser(schemaFormat: SchemaFormat) = when (schemaFormat) {
    SchemaFormat.jsonschema -> JsonSSchemaParser()
    else -> JsonSSchemaParser()
}