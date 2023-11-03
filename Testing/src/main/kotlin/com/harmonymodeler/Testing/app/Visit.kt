package com.harmonymodeler.Testing.app

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File


fun visit(path: String, filter: String) = runBlocking {
    File(path).walk()
            .filter { file -> file.name.contains(filter) }
            .forEach {
                launch {
                    visitFile(it)
                }
            }
}

fun visit(schemaRepository: SchemaRepository) = runBlocking {
    File(schemaRepository.rootPath).walk()
    
    val age = matchResult.groups["age"]?.value
    val name = matchResult.groups["name"]?.value

    assertEquals("Mickey Mouse", name)
    assertEquals("95", age)


//            .filter { file -> file.name.contains(filter) }
//            .forEach {
//                launch { 
//                    visitFile(it)
//                }
//            }
}

fun visitFile(path: File) {
    println(path)
}