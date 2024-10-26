package net.orangedog.revan.plugins

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.server.application.*
import net.orangedog.revan.models.trubar.SloleksWordRepository
import net.orangedog.revan.models.trubar.SloleksWordRepositoryImpl
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureDependencyInjection(db: MongoDatabase) {

    val appModule = module {
        single { db }
    }

    val trubarModule = module {
        single<SloleksWordRepository> { SloleksWordRepositoryImpl(get()) }
    }

    install(Koin) {
        slf4jLogger()
        modules(appModule, trubarModule)
    }
}