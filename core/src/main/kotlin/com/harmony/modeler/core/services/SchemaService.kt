package com.harmony.modeler.core.services

import com.harmony.modeler.core.models.schema.Schema
import java.util.LinkedList

class SchemaService {
    companion object {

        /**
         * Explode a schema by returning all the definitions and sub-definitions
         */
        fun explode(schema: Schema): List<Schema> {
            val results = mutableListOf<Schema>()
            val queue = LinkedList<Schema>()
            queue.push(schema)
            results.add(schema)
            while (!queue.isEmpty()) {
                val toExplode = queue.poll()
                toExplode.schemaFile = schema.schemaFile
                if (toExplode.fields != null) {
                    results.addAll(toExplode.fields)
                    queue.addAll(toExplode.fields)
                }
                if (toExplode.items != null) {
                    results.addAll(toExplode.items)
                    queue.addAll(toExplode.items)
                }
                if (toExplode.possibleTypes != null) {
                    results.addAll(toExplode.possibleTypes)
                    queue.addAll(toExplode.possibleTypes)
                }
            }
            return results
        }
    }
}