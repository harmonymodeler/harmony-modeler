package com.harmony.modeler.core.repositories

import com.harmony.modeler.core.models.schema.Schema
import java.util.LinkedList

public class InMemorySchemaRepository {
    val inMemoryMetaDb: MutableSet<Schema> = HashSet()

    // for comparison
    val inMemoryDbList: MutableList<Schema> = mutableListOf()

    fun addAll(schemaList: List<Schema>) {
        val queue = LinkedList(schemaList)
        while (!queue.isEmpty()) {
            val schema = queue.poll()
            inMemoryMetaDb.add(schema)
            inMemoryDbList.add(schema)

            schema.fields?.forEach {
                inMemoryMetaDb.add(it)
                inMemoryDbList.add(it)
                queue.add(it)
            }
        }
    }

    fun listAll(): Set<Schema> {
        return inMemoryMetaDb
    }



}