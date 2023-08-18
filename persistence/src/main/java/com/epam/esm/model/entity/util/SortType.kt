package com.epam.esm.model.entity.util

import com.epam.esm.exception.InvalidParameterException

enum class SortType {
    ASC,
    DESC;

    companion object {
        fun findSortType(sortType: String): SortType {
            val sortType1: SortType = try {
                valueOf(sortType.uppercase())
            } catch (e: Exception) {
                throw InvalidParameterException("invalid-sortparameter")
            }
            return sortType1
        }
    }
}
