package com.harmony.modeler.core.repositories

import com.harmony.modeler.core.models.schema.Schema
import com.harmony.modeler.core.services.SchemaService
import java.util.LinkedList

public class InMemorySchemaRepository {
    val inMemoryMetaDb: MutableSet<Schema> = HashSet()

    // for comparison
    val inMemoryDbList: MutableList<Schema> = mutableListOf()

    fun addAll(schemaList: List<Schema>) {
        val queue = LinkedList(
            schemaList.flatMap { SchemaService.explode(it)
        })
        
        while (!queue.isEmpty()) {
            val schema = queue.poll()
            inMemoryMetaDb.add(schema)
            inMemoryDbList.add(schema)
        }
    }

    fun listAll(): Set<Schema> {
        return inMemoryMetaDb
    }
    
    /// je veux retourner un top hit en gros, partout ou ces schemas sont utilisés, en terme de count ?
    fun groupCanonical() {
        
    }



}