package net.orangedog.revan.models.common

fun Any?.ensureNotSet(name: String) {
    if (this != null) {
        throw IllegalArgumentException("$name is already set")
    }
}