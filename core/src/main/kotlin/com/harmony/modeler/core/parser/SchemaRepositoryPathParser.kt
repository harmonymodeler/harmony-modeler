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
    companion object {

        public fun createMatcher(pattern: String, stringToTest: String): Matcher {
            val captureMatcher = Pattern.compile(pattern)
                .matcher(stringToTest)
            captureMatcher.find()
            return captureMatcher
        }

        fun parsePathString(basePath: String, path: String, discoveryPattern: DiscoveryPattern) {

        }

        fun parsePath(basePath: Path, path: Path, discoveryPattern: DiscoveryPattern): SchemaFile {
            val schemaFile = SchemaFile.builder()
                .filePath(basePath.relativize(path).toString())
                .format(SchemaFormat.valueOf(path.extension))
                .schema(Files.readString(path))
                .matchedBy(discoveryPattern)
                .qualifier("")
                .build()

            val captureMatcher = createMatcher(discoveryPattern.capture, schemaFile.filePath)

            val name = captureGroup("name", captureMatcher)
                ?.replace("-"," ")
                ?.trim()

            return schemaFile
                .withName(name)
                .withDomain(captureGroup("domain", captureMatcher))
                .withVersion(captureGroup("version", captureMatcher))
        }

        private fun captureGroup(name: String, matcher: Matcher): String? {
            return try {
                val extracted = matcher.group(name)
                if (extracted.isEmpty()) {
                    return null
                }
                return extracted
            } catch (exception: Exception) {
                null;
            }
        }
    }
}