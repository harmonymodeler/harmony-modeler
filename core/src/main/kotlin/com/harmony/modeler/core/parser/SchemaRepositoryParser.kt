package com.harmony.modeler.core.parser

import arrow.core.Either
import com.harmony.modeler.core.services.FileLoader
import com.harmony.modeler.core.models.repository.SchemaRepository
import com.harmony.modeler.core.models.schema.Schema
import com.harmony.modeler.core.models.schema.SchemaFormat
import java.io.File
import java.net.URI
import java.nio.file.Paths
import java.util.regex.Pattern
import kotlin.io.path.absolutePathString

class SchemaRepositoryParser {
    companion object {
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

        fun parse(schemaRepository: SchemaRepository, file: File): List<Schema> {
            val matchingPattern = schemaRepository.discoveryPatterns.find {
                Pattern.compile(it.capture).matcher(file.absolutePath).matches()
            }
            val basePath = Paths.get(schemaRepository.localPath)

            if (matchingPattern != null) {
                val schemaFile = SchemaRepositoryPathParser.parsePath(basePath, file.toPath(), matchingPattern)

                try {
                    // du génie
                    return SchemaParser(schemaFile.format)
                        .parse(basePath, schemaFile)
                } catch (e: Exception) {
                    return listOf(
                        Schema.builder()
                            .schemaFile(schemaFile)
                            .errors(listOf(e))
                            .build()
                    )
                }
            }
            return emptyList()
        }

        fun parse(schemaRepository: SchemaRepository): Sequence<Schema> {
            if (schemaRepository.localPath != null) {
//                val repositoryPath = schemaRepository.localPath + "/" + schemaRepository.rootPath
                val repositoryPath = Paths.get(schemaRepository.localPath, schemaRepository.rootPath)
                return File(repositoryPath.toString())
                    .walk()
                    .filter {
                        it.isFile
                    }
                    .flatMap {
                        parse(schemaRepository, it)
                    }
            }
            return emptySequence()
        }
    }
}