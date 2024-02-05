package com.harmony.modeler.core.parser

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.harmony.modeler.core.models.repository.DiscoveryPattern
import com.harmony.modeler.core.models.repository.SchemaRepository
import com.harmony.modeler.core.models.schema.SchemaFile
import com.harmony.modeler.core.models.schema.SchemaFormat
import com.harmony.modeler.core.repositories.InMemorySchemaRepository
import com.harmony.modeler.core.services.SchemaService
import io.github.oshai.kotlinlogging.KotlinLogging
import io.kotest.inspectors.forAll
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.nio.file.Files
import java.nio.file.Paths

class SchemaRepositoryParserTest {
    
    private val logger = KotlinLogging.logger {}

    companion object {
        val schemaStoreBasePath = "/home/acouty/git/Personnel/harmony-modeler/src/schemas/json"

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
    }

    @Test
    fun testParseRepository (){
        val schemaRepository = SchemaRepository.builder()
                    .name("schemastore")
                    .localPath("/home/acouty/git/Personnel/schemastore")
                    .url("")
                    .rootPath("src/schemas/json")
                    .discoveryPatterns(
                        listOf(DiscoveryPattern.builder()
                        .name("most")
                        .capture(".*/schemas/json/(?<domain>[a-z]+){0,1}(?<name>(-[a-z]+)*)(-(?<version>([0-9.]+)*))*.*.json")
                        .build()))
                    .build()
        
        val result = SchemaRepositoryParser.parse(schemaRepository).toList()
        val db = InMemorySchemaRepository()
        
        db.addAll(result)
        
        val sorted = db.usage.toSortedMap(compareByDescending { db.usage[it]?.get() })
        val sorted2 = db.usage.toList()
            .sortedByDescending { it.second.get() }
        
        assertThat(db.inMemoryDbList.size).isEqualTo(77840)
        
        logger.info { "" }
    }

}