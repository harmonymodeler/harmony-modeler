package com.harmony.modeler.core.parser

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.harmony.modeler.core.models.repository.DiscoveryPattern
import com.harmony.modeler.core.models.schema.SchemaFile
import com.harmony.modeler.core.models.schema.SchemaFormat
import com.harmony.modeler.core.repositories.InMemorySchemaRepository
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
            ),
            Arguments.of(
                // should find out what peculiarity this has
                Files.readString(Paths.get(schemaFeaturesDir, "liquibase-3.2.draft-07.json"))
            )
            // TODO remove below
            ,
            Arguments.of(
                Files.readString(Paths.get(schemaFeaturesDir, "sarif-2.1.0-rtm.3.json"))
            )
        )

    }

    //    @ParameterizedTest
//    @MethodSource("patternsMatch")
    @Test
    fun testParse() {
        val parser = SchemaParser(SchemaFormat.json)
        val inputSchema = Files.readString(Paths.get(schemaFeaturesDir, "simple-car-with-reference.draft-07.json"))

        val result = parser.parse(inputSchema)
        val db = InMemorySchemaRepository()
        
        db.addAll(result)
        val exploded = SchemaService.explode(result[0])
        println("")
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
        
        val db = InMemorySchemaRepository()
        
        db.addAll(result)
        
        val sorted = db.usage.toSortedMap(compareByDescending { db.usage[it]?.get() })
        val sorted2 = db.usage.toList()
            .sortedByDescending { it.second.get() }
        
        val resultString = ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .setSerializationInclusion(Include.NON_NULL)
            .writeValueAsString(result)
        
        

//        assertAll(
//            { assertThat(exploded).hasSize(10) }
//        )
    }


}