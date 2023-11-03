package com.harmonymodeler.Testing.app

data class Repository(
        val url: String 
)

data class SchemaRepository(
        val rootPath: String,
        val filter: String,
        val patterns: Array<Pattern> = emptyArray()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SchemaRepository

        if (rootPath != other.rootPath) return false
        if (filter != other.filter) return false
        if (!patterns.contentEquals(other.patterns)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = rootPath.hashCode()
        result = 31 * result + filter.hashCode()
        result = 31 * result + patterns.contentHashCode()
        return result
    }
}

data class Pattern(val expression: Regex)

