package com.desuzed.everyweather.domain.repository.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

abstract class BaseDatastoreApiProvider {
    abstract val dataStore: DataStore<Preferences>

    suspend inline fun edit(crossinline transform: suspend (MutablePreferences) -> Unit) {
        dataStore.edit { preferences ->
            transform(preferences)
        }
    }

    fun <V> getFlowOf(key: Preferences.Key<V>, defaultValue: V): Flow<V> =
        dataStore.data.map { it[key] ?: defaultValue }

}