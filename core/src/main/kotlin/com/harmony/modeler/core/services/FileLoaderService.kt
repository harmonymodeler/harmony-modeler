package com.harmony.modeler.core.services

import com.fasterxml.jackson.core.exc.StreamReadException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.harmony.modeler.core.models.schema.SchemaFormat
import java.nio.file.Path
import java.security.cert.Extension

interface FileLoader {
    fun load(path: Path): String
}

fun FileLoader(format: SchemaFormat) = when (format) {
    SchemaFormat.yaml -> YamlLoader()
    else -> JsonLoader()
}

class YamlLoader : FileLoader {
    private val yamlMapper = ObjectMapper(YAMLFactory())
    private val jsonMapper = ObjectMapper()

    override fun load(path: Path): String {
        val schemaObject = yamlMapper.readValue(path.toFile(), Object::class.java)
        return jsonMapper.writeValueAsString(schemaObject)
    }
}

class JsonLoader : FileLoader {
    private val jsonMapper = ObjectMapper()

    override fun load(path: Path): String {
        val schemaObject = jsonMapper.readValue(path.toFile(), Object::class.java)
        return jsonMapper.writeValueAsString(schemaObject)
    }
}