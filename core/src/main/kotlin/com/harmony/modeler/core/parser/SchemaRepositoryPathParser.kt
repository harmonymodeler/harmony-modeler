package com.harmony.modeler.core.parser

import com.harmony.modeler.core.models.repository.DiscoveryPattern
import com.harmony.modeler.core.models.repository.SchemaRepository
import com.harmony.modeler.core.models.schema.SchemaFile
import com.harmony.modeler.core.models.schema.SchemaFormat
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.io.path.extension

interface SchemaRepositoryPathParser {



    fun parse(schema: String): SchemaFile

    companion object {
        fun parsePath(path: Path, discoveryPattern: DiscoveryPattern): SchemaFile {
            val schemaFile = SchemaFile.builder()
                .filePath(path.toAbsolutePath().toString())
                .format(SchemaFormat.valueOf(path.extension))
                .schema(Files.readString(path))
                .qualifier("")
                .build()

            val captureMatcher = Pattern.compile(discoveryPattern.capture)
                .matcher(schemaFile.filePath)

            return schemaFile
                .withName(captureGroup("name", captureMatcher))
                .withDomain(captureGroup("domain", captureMatcher))
                .withVersion(captureGroup("version", captureMatcher))
        }

        private fun shouldParseInfos(schemaRepository: SchemaRepository): Boolean {
            return schemaRepository.name == null || schemaRepository.organization == null
        }

        fun parseInfos(schemaRepository: SchemaRepository): SchemaRepository {
            val uri = URI(schemaRepository.url)
            var name = schemaRepository.name
            var organization = schemaRepository.organization

            if (shouldParseInfos(schemaRepository)) {
                val paths = uri.path.split("/")
                name = paths[1]
                organization = paths[2]
            }

            return schemaRepository
                .withName(name)
                .withOrganization(organization)
        }

        private fun captureGroup(name: String, matcher: Matcher): String? {
            return try {
                matcher.group(name)
            } catch (exception: IllegalStateException) {
                null;
            }
        }

        fun parse(schemaFile: SchemaFile, discoveryPattern: DiscoveryPattern): SchemaFile? {
            val captureMatcher = Pattern.compile(discoveryPattern.capture)
                .matcher(schemaFile.filePath)

            return schemaFile
                .withName(captureGroup("name", captureMatcher))
                .withDomain(captureGroup("domain", captureMatcher))
                .withVersion(captureGroup("version", captureMatcher))
        }
    }
}