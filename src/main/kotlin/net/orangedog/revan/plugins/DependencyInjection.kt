package net.orangedog.revan.plugins

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.server.application.*
import net.orangedog.revan.modules.trubar.TrubarModuleConfig
import net.orangedog.revan.modules.trubar.importer.SloleksXmlImporter
import net.orangedog.revan.repository.trubar.SloleksWordRepository
import net.orangedog.revan.repository.trubar.SloleksWordRepositoryImpl
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureDependencyInjection(
    db: MongoDatabase,
    trubarConfig: TrubarModuleConfig,
) {

    val appModule = module {
        single { db }
    }

    val trubarModule = module {
        single { trubarConfig }
        single<SloleksWordRepository> { SloleksWordRepositoryImpl(get()) }
        factory<SloleksXmlImporter> { SloleksXmlImporter(get()) }
    }

    install(Koin) {
        slf4jLogger()
        modules(appModule, trubarModule)
    }
}