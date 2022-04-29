package com.example.wordle_compose.data.model

import androidx.datastore.core.Serializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
object GameStatSerializer : Serializer<GameStats> {
    override val defaultValue: GameStats
        get() = GameStats()

    override suspend fun readFrom(input: InputStream): GameStats {
        return try {
            Json.decodeFromString(
                deserializer = GameStats.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: GameStats, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = GameStats.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}