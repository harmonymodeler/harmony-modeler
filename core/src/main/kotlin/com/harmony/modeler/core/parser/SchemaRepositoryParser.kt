package com.harmony.modeler.core.parser

import com.harmony.modeler.core.services.FileLoader
import com.harmony.modeler.core.models.repository.SchemaRepository
import com.harmony.modeler.core.models.schema.Schema
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
import java.util.regex.Pattern
import kotlin.io.path.isDirectory

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

        fun parse(schemaRepository: SchemaRepository): MutableList<Schema> {
            val myList: MutableList<Schema> = mutableListOf()

            if (schemaRepository.localPath != null) {
                val repositoryPath = Paths.get(schemaRepository.localPath, schemaRepository.rootPath)

                Files.walk(repositoryPath)
                    .filter {
                        !it.isDirectory()
                    }
                    .flatMap {
                        path -> schemaRepository.discoveryPatterns.map {
                                Triple(path, it, Pattern.compile(it.capture).matcher(path.toAbsolutePath().toString()).matches())
                            }.stream()
                    }
                    .filter {
                        it.third
                    }
                    .forEach{
                        val schemaFile = SchemaRepositoryPathParser.parsePath(it.first, it.second)
                        try {
                            val schemaString = FileLoader(schemaFile.format).load(it.first)
                            val schema = SchemaParser(schemaFile.format).parse(schemaString)
                            myList.addAll(schema)
                        } catch (e: Exception) {
                            println("${schemaFile.filePath} : ${e.message}")
                        }
                    }
            }
            return myList
        }
    }
}