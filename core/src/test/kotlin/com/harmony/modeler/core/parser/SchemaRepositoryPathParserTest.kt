package com.harmony.modeler.core.parser

import com.harmony.modeler.core.models.repository.DiscoveryPattern
import com.harmony.modeler.core.models.schema.SchemaFile
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.nio.file.Paths

class SchemaRepositoryPathParserTest {

    companion object {
        val schemaStoreDiscovery = ".*/schemas/json/(?<domain>[a-z]+){0,1}(?<name>(-[a-z]+)*)(-(?<version>([0-9.]+)*))*.*.json"
        val schemaStoreBasePath = "src/test/resources/repositories/schemastore"


        @JvmStatic
        fun patternsMatch() = listOf(
            // pattern, basePath, tested, expected
            Arguments.of(
                DiscoveryPattern.builder()
                    .capture(schemaStoreDiscovery)
                    .build(),
                schemaStoreBasePath,
                "src/test/resources/repositories/schemastore/src/schemas/json/foundryvtt-base-package-manifest.json",
                SchemaFile.builder()
                    .name("base package manifest")
                    .domain("foundryvtt")
                    .build()
            ),
            Arguments.of(
                DiscoveryPattern.builder()
                    .capture(schemaStoreDiscovery)
                    .build(),
                schemaStoreBasePath,
                "src/test/resources/repositories/schemastore/src/schemas/json/accelerator.json",
                SchemaFile.builder()
                    .domain("accelerator")
                    .build()
            ),
            Arguments.of(
                DiscoveryPattern.builder()
                    .capture(schemaStoreDiscovery)
                    .build(),
                schemaStoreBasePath,
                "src/test/resources/repositories/schemastore/src/schemas/json/agripparc-1.2.json",
                SchemaFile.builder()
                    .domain("agripparc")
                    .version("1.2")
                    .build()
            ),
            Arguments.of(
                DiscoveryPattern.builder()
                    .capture(schemaStoreDiscovery)
                    .build(),
                schemaStoreBasePath,
                "src/test/resources/repositories/schemastore/src/schemas/json/airlock-microgateway-3.1.json",
                SchemaFile.builder()
                    .domain("airlock")
                    .name("microgateway")
                    .version("3.1")
                    .build()
            ),
            Arguments.of(
                DiscoveryPattern.builder()
                    .capture(schemaStoreDiscovery)
                    .build(),
                schemaStoreBasePath,
                "src/test/resources/repositories/schemastore/src/schemas/json/azure-iot-edgehub-deployment-1.0.json",
                SchemaFile.builder()
                    .domain("azure")
                    .name("iot edgehub deployment")
                    .version("1.0")
                    .build()
            ),
            Arguments.of(
                DiscoveryPattern.builder()
                    .capture(schemaStoreDiscovery)
                    .build(),
                schemaStoreBasePath,
                "src/test/resources/repositories/schemastore/src/schemas/json/detekt-1.22.0.json",
                SchemaFile.builder()
                    .domain("detekt")
                    .version("1.22.0")
                    .build()
            ),

        )

    }

    @ParameterizedTest
    @MethodSource("patternsMatch")
    fun testSomeMatches(pattern: DiscoveryPattern, basePath: String, tested: String, expected: SchemaFile) {


        val result = SchemaRepositoryPathParser.parsePath(Paths.get(basePath), Paths.get(tested), pattern)

        assertThat(result).isNotNull
        assertAll(
            { assertThat(result.name).`as`("name").isEqualTo(expected.name) },
            { assertThat(result.domain).`as`("domain").isEqualTo(expected.domain) },
            { assertThat(result.version).`as`("version").isEqualTo(expected.version) },
        )
    }



}