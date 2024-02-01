package com.harmony.modeler.core.parser

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.harmony.modeler.core.models.repository.DiscoveryPattern
import com.harmony.modeler.core.models.schema.SchemaFile
import com.harmony.modeler.core.models.schema.SchemaFormat
import com.harmony.modeler.core.services.SchemaService
import io.kotest.inspectors.forAll
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.nio.file.Files
import java.nio.file.Paths

class SchemaParserTest {

    companion object {
        val schemaStoreBasePath = "src/test/resources/repositories/schemastore/src/schemas/json"
        val schemaFeaturesDir = "src/test/resources/schemas_features"


        @JvmStatic
        fun patternsMatch() = listOf(
            ""
            // pattern, basePath, tested, expected
//            Arguments.of(
//                DiscoveryPattern.builder()
//                    .capture(schemaStoreDiscovery)
//                    .build(),
//                schemaStoreBasePath,
//                "src/test/resources/repositories/schemastore/src/schemas/json/foundryvtt-base-package-manifest.json",
//                SchemaFile.builder()
//                    .name("base package manifest")
//                    .domain("foundryvtt")
//                    .build()
//            )
        )

        @JvmStatic
        fun cicularSchemas() = listOf(
            Arguments.of(
                Files.readString(Paths.get(schemaFeaturesDir, "circular-references.draft-07.json"))
            ),
            Arguments.of(
                Files.readString(Paths.get(schemaFeaturesDir, "circular-references.2020-12.json"))
            )
        )

    }

    //    @ParameterizedTest
//    @MethodSource("patternsMatch")
    @Test
    fun testParse() {
        val parser = SchemaParser(SchemaFormat.json)
        val inputSchema = Files.readString(Paths.get(schemaStoreBasePath, "accelerator.json"))

        val result = parser.parse(inputSchema)

        val exploded = SchemaService.explode(result[0])

    }

    //    @Test
    @ParameterizedTest
    @MethodSource("cicularSchemas")
    fun testParseCircularReferences(inputSchemaString: String) {
        val parser = SchemaParser(SchemaFormat.json)

        val result = parser.parse(inputSchemaString)

        assertThat(result).isNotEmpty
        assertThat(result).hasSize(1)

        val exploded = SchemaService.explode(result[0])
//        exploded.forAll { assertThat(it.version).isEqualTo("") }

        val resultString = ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .setSerializationInclusion(Include.NON_NULL)
            .writeValueAsString(result)

        assertAll(
            { assertThat(exploded).hasSize(10) }
        )
    }


}