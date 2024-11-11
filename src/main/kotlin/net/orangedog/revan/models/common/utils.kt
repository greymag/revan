package net.orangedog.revan.models.common

inline fun <reified T : Enum<T>> getEnumValue(value: String, predicate: (T) -> String): T {
    return enumValues<T>().firstOrNull { predicate(it) == value } ?: throw IllegalArgumentException("Unknown value: <$value> for enum ${T::class.simpleName}")
}
