package com.epam.esm.model.entity.util

class QueryParameters(
    var partOfName: String,
    var partOfDescription: String,
    tagNames: List<String?>,
    sortNameParam: SortType?,
    sortDateParam: SortType?
) {
    var tagNames: List<String?>? = null
    var sortNameParam: SortType? = null
    var sortDateParam: SortType? = null

    init {
        if (!tagNames.isEmpty()) {
            this.tagNames = tagNames
        }
        if (sortNameParam != null) {
            this.sortNameParam = sortNameParam
        }
        if (sortDateParam != null) {
            this.sortDateParam = sortDateParam
        }
    }
}