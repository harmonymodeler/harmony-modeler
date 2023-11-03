package com.harmonymodeler.Testing.app

import org.junit.jupiter.api.Test

class VisitTest {
    
    @Test
    fun visitTest() {
        visit("/home/acouty/personnel/harmony-modeler/.data", ".json")
    }
    
    @Test
    fun jeReflechis() {
        val repo = SchemaRepository("/home/acouty/personnel/harmony-modeler/.data/schemastore", "*.json")
        visit(repo)
    }
}