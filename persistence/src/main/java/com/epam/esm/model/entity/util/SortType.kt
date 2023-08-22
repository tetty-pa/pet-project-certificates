package com.epam.esm.model.entity.util

import com.epam.esm.exception.InvalidParameterException

enum class SortType {
    ASC,
    DESC;

    companion object {
        fun findSortType(sortType: String): SortType {
            return try {
                valueOf(sortType.uppercase())
            } catch (e: Exception) {
                throw InvalidParameterException("invalid-sortparameter")
            }
        }
    }
}
