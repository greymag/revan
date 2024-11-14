package net.orangedog.revan.modules.trubar

import io.ktor.server.config.*

data class TrubarModuleConfig(
    val importWordsDir: String?,
) {
    companion object {
        fun from(config: ApplicationConfig): TrubarModuleConfig {
            return TrubarModuleConfig(
                importWordsDir = config.propertyOrNull("trubar.import.wordsDir")?.getString(),
            )
        }
    }
}