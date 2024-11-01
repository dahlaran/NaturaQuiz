package com.dahlaran.naturaquiz.core.extensions

fun <T> List<T>.firstOrNullFromIndex(startIndex: Int, predicate: (T) -> Boolean): T? {
    for (i in startIndex until this.size) {
        if (predicate(this[i])) {
            return this[i]
        }
    }
    return null
}