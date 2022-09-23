package com.bartozo.lifeprogress.ui.appwidgets

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.datastore.dataStoreFile
import androidx.glance.state.GlanceStateDefinition
import com.bartozo.lifeprogress.data.Life
import com.bartozo.lifeprogress.data.LifeState
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.File
import java.io.InputStream
import java.io.OutputStream

/**
 * Provides our own definition of "Glance state" using Kotlin serialization.
 */
object LifeProgressStateDefinition : GlanceStateDefinition<LifeState> {

    private const val DATA_STORE_FILENAME = "life_progress_widget"
    private const val STORE_NAME = "life_progress_widget_data_store"
    private val Context.datastore: DataStore<LifeState> by dataStore(
        fileName = STORE_NAME,
        serializer = LifeCalendarSerializer
    )

    override suspend fun getDataStore(context: Context, fileKey: String): DataStore<LifeState> {
        return context.datastore
    }

    override fun getLocation(context: Context, fileKey: String): File {
        return context.dataStoreFile(DATA_STORE_FILENAME)
    }

    /**
     * Custom serializer for Life using Json.
     */
    object LifeCalendarSerializer : Serializer<LifeState> {
        override val defaultValue = LifeState.Unavailable("No't working :(")

        override suspend fun readFrom(input: InputStream): LifeState = try {
            Json.decodeFromString(
                LifeState.serializer(),
                input.readBytes().decodeToString()
            )
        } catch (exception: SerializationException) {
            throw CorruptionException("Could not read data: ${exception.message}")
        }

        override suspend fun writeTo(t: LifeState, output: OutputStream) {
            output.use {
                it.write(
                    Json.encodeToString(LifeState.serializer(), t).encodeToByteArray()
                )
            }
        }
    }
}