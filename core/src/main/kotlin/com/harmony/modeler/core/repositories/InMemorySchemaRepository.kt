package com.harmony.modeler.core.repositories

import arrow.atomic.AtomicLong
import com.harmony.modeler.core.models.schema.Schema
import com.harmony.modeler.core.services.SchemaService
import java.util.LinkedList

data class SchemaStats(val schema: Schema, val hits: AtomicLong, ) {}

public class InMemorySchemaRepository {
    
    val inMemoryMetaDb: MutableSet<Schema> = HashSet()
    val usage: MutableMap<Schema, AtomicLong> = HashMap()

    // for comparison
    val inMemoryDbList: MutableList<Schema> = mutableListOf()

    fun addAll(schemaList: List<Schema>) {
        val queue = LinkedList(
            schemaList.flatMap { SchemaService.explode(it)
        })
        
        while (!queue.isEmpty()) {
            val schema = queue.poll()
            if (usage[schema] == null)
                usage[schema] = AtomicLong(1)
            else
                usage[schema]?.addAndGet(1)
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