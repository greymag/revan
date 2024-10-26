package net.orangedog.revan.plugins

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.server.application.*

fun Application.configureDatabases(connectionString: String): MongoDatabase {
    val mongoClient = MongoClient.create(connectionString)
    val database = mongoClient.getDatabase("revan")

    // TODO: close the client when the application is stopped?

    return database
}