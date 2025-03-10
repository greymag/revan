package net.orangedog.revan.errors

sealed class RevanError(
    val domain: String,
    val code: Int,
) {
    fun toDto() = ErrorDto(domain, code)
}

abstract class CommonError(code: Int) : RevanError("Global", code) {
    object System : CommonError(0)

    object NotFound : CommonError(1)

    object BadRequest : CommonError(2)
}

