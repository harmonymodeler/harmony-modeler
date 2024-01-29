package com.harmony.modeler.core.parser

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.harmony.modeler.core.models.schema.Schema
import com.jayway.jsonpath.JsonPath
import java.util.*
import java.util.stream.StreamSupport
import kotlin.collections.HashSet

class JsonSSchemaParser : SchemaParser {

    fun parseProperties(jsonProperties: JsonNode?): MutableList<Schema>? {
        var fields: MutableList<Schema>? = null
        if (jsonProperties != null) {
            fields = StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(jsonProperties.fields().iterator(), Spliterator.ORDERED),
                false
            )
                .map {
                    val schema = parse(it.value)
                    schema.name = it.key
                    schema
                }
                .toList()
        }
        return fields
    }

    fun parseEnum(jsonEnum: JsonNode?): MutableList<String>? {
        if (jsonEnum != null && jsonEnum.isArray) {
            val enum = mutableListOf<String>()
            jsonEnum.forEach {
                enum.add(it.asText())
            }
            return enum
        }
        return null
    }

    fun parse(jsonNode: JsonNode): Schema {
        return Schema.builder()
            .name(jsonNode.get("title")?.asText())
            .namespace(jsonNode.get("\$id")?.asText())
            .description(jsonNode.get("description")?.asText())
            .id(jsonNode.get("\$id")?.asText())
            .format(jsonNode.get("format")?.asText())
            .type(jsonNode.get("type")?.asText())
            .version(jsonNode.get("version")?.asText())
            .fields(parseProperties(jsonNode.get("properties")))
            .enumeration(parseEnum(jsonNode.get("enum")))
            .schema(jsonNode.toString())
            .build();
    }

    override fun parse(schema: String): List<Schema> {
        val objectMapper = ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        val schemaObject = objectMapper.readTree(schema)
        return listOf(parse(schemaObject))
    }
}
