package com.harmonymodeler.api.controllers

import com.harmony.modeler.core.parser.SchemaRepositoryParser
import com.harmony.modeler.core.models.repository.SchemaRepository
import com.harmony.modeler.core.models.schema.Schema
import com.harmonymodeler.api.repositories.SchemaRepositoryRepository
import kotlinx.coroutines.flow.Flow
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/schema-repositories")
class SchemaRepositoryController(val schemaRepositoryRepository: SchemaRepositoryRepository) {

    @GetMapping
    @ResponseBody
    fun listAll(): Flow<SchemaRepository> {
        return this.schemaRepositoryRepository.findAll();
    }

    @PostMapping
    @ResponseBody
    suspend fun save(@RequestBody schemaRepository: SchemaRepository): SchemaRepository {
        return this.schemaRepositoryRepository.save(schemaRepository);
    }

    @PostMapping("/parse-infos")
    @ResponseBody
    fun parseInfos(@RequestBody schemaRepository: SchemaRepository): SchemaRepository {
        return SchemaRepositoryParser.parseInfos(schemaRepository);
    }

    @PostMapping("/parse")
    @ResponseBody
    fun parse(@RequestBody schemaRepository: SchemaRepository): Sequence<Schema> {
        return SchemaRepositoryParser.parse(schemaRepository);
    }


}