package com.epam.esm.model.entity.util

class QueryParameters(
    val partOfName: String,
    val partOfDescription: String,
    tagNames: List<String?>,
    val sortNameParam: SortType?,
    val sortDateParam: SortType?
) {
    var tagNames: List<String?>? = null

    init {
        if (tagNames.isNotEmpty()) {
            this.tagNames = tagNames
        }
    }
}
