package com.harmony.modeler.core.parser

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.harmony.modeler.core.models.schema.Schema
import com.harmony.modeler.core.models.schema.SchemaFile
import com.harmony.modeler.core.models.schema.SchemaFormat
import com.harmony.modeler.core.services.FileLoader
import com.jayway.jsonpath.JsonPath
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.regex.Pattern
import java.util.stream.StreamSupport
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class JsonSSchemaParser : SchemaParser {

    fun parseProperties(jsonProperties: JsonNode?, rootNode: JsonNode, evaluateRef: Boolean): MutableList<Schema>? {
        var fields: MutableList<Schema>? = null
        if (jsonProperties != null) {
            fields = StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(jsonProperties.fields().iterator(), Spliterator.ORDERED),
                false
            )
                .map {
                    val schema = parse(it.value, rootNode, evaluateRef)
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

    fun parseVersion(jsonSchemaDialectUrl: String): String {
        val matcher = Pattern.compile("""org/(?<version>.*)/""")
            .matcher(jsonSchemaDialectUrl)
        matcher.find()
        return matcher.group("version")
    }

    /**
     * Parse arrayNode of schemas
     */
    fun parseArray(jsonNode: JsonNode?, rootNode: JsonNode, evaluateRef: Boolean = true): List<Schema>? {
        if (jsonNode != null) {
            when(jsonNode) {
                is ArrayNode -> {
                    return StreamSupport.stream(
                        Spliterators.spliteratorUnknownSize(jsonNode.elements(), Spliterator.ORDERED),false)
                        .filter { !isAlreadyParsedReference(it) }
                        .map { parse(it, rootNode, evaluateRef) }
                        .toList()
                }
                else -> return listOf(parse(jsonNode, rootNode, evaluateRef))
            }
        }
        return null
    }

    fun getReference(jsonNode: JsonNode): JsonNode? {
        return jsonNode.get("\$ref")
    }

    fun isReference(jsonNode: JsonNode): Boolean {
        return getReference(jsonNode) != null
    }

    fun isAlreadyParsedReference(jsonNode: JsonNode): Boolean {
        return isReference(jsonNode) && (alreadyRefed[getReference(jsonNode)?.asText()] != null)
    }

    val alreadyRefed = HashMap<String, Schema>()

    val reffingStack = HashSet<String>()

    fun parse(jsonNode: JsonNode, rootNode: JsonNode, evaluateRef: Boolean=true): Schema {
        if (evaluateRef && jsonNode.get("\$ref") != null) {
            val ref = jsonNode.get("\$ref").asText()
            if (ref != null && (ref.startsWith("#/definitions") || ref.startsWith("#/\$defs"))) {
                val refHoldingNode = ref.split("/")[1]
                val schemaName = ref.substring(ref.lastIndexOf("/") + 1)
                val refNode = rootNode
                    .get(refHoldingNode)
                    .get(schemaName)
                println("Handling ref $ref")
                val shouldEvaluateNextRefs = !reffingStack.contains(ref)
                reffingStack.add(ref)
                val schema = parse(refNode, rootNode, shouldEvaluateNextRefs)
                schema.name = schemaName
                reffingStack.remove(ref)
                alreadyRefed[ref] = schema
                return schema
            }
        }

        val formatVersionNode = jsonNode.get("\$schema")
        var formatVersion: String? = null
        if (formatVersionNode != null) {
            formatVersion = parseVersion(formatVersionNode.asText())
        }

        val type = jsonNode.get("type")?.asText()
        if (type != null) {
            if (type.equals("array")) {

            }
        }

        var itemsNode = jsonNode.get("items")

        if (itemsNode == null) {
            itemsNode = jsonNode.get("prefixItems")
        }
        val items = parseArray(itemsNode, rootNode, evaluateRef)

        var possibleTypes = mutableListOf<Schema>()

        val anyOfNode = jsonNode.get("anyOf")
        if (anyOfNode != null) {
            val anyOf = parseArray(jsonNode.get("anyOf"), rootNode, evaluateRef)
            if (anyOf !=  null) {
                possibleTypes.addAll(anyOf)
            }
        }

        val allOfNode = jsonNode.get("allOf")
        if (allOfNode != null) {
            val allOf = parseArray(jsonNode.get("allOf"), rootNode, evaluateRef)
            if (allOf !=  null) {
                possibleTypes.addAll(allOf)
            }
        }

        val oneOfNode = jsonNode.get("oneOf")
        if (oneOfNode != null) {
            val oneOf = parseArray(jsonNode.get("oneOf"), rootNode, evaluateRef)
            if (oneOf !=  null) {
                possibleTypes.addAll(oneOf)
            }
        }

        return Schema.builder()
            .name(jsonNode.get("title")?.asText())
            .format(SchemaFormat.json.value())
            .namespace(jsonNode.get("\$id")?.asText())
            .description(jsonNode.get("description")?.asText())
            .id(jsonNode.get("\$id")?.asText())
            .format(jsonNode.get("format")?.asText())
            .type(type)
            .possibleTypes(if (possibleTypes.isEmpty()) null else possibleTypes)
            .items(items)
            .version(jsonNode.get("version")?.asText())
            .fields(parseProperties(jsonNode.get("properties"), rootNode, evaluateRef))
            .enumeration(parseEnum(jsonNode.get("enum")))
            .formatVersion(formatVersion)
            .schema(jsonNode.toString())
            .build();
    }

    fun parse(jsonNode: JsonNode): Schema {
        return parse(jsonNode, jsonNode)
    }

    override fun parse(schema: String): List<Schema> {
        val objectMapper = ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        val schemaObject = objectMapper.readTree(schema)

        return listOf(parse(schemaObject))
    }

    override fun parse(basePath: Path, schemaFile: SchemaFile): List<Schema> {
        val schemaString = FileLoader(schemaFile.format)
            .load(Paths.get(basePath.toString(), schemaFile.filePath))
        return parse(schemaString)
            .map { it.withSchemaFile(schemaFile) }
    }
}
