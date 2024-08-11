package com.desuzed.everyweather.domain.repository.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.desuzed.everyweather.domain.repository.local.BaseDatastoreApiProvider

class SettingsDatastoreApiProvider(private val context: Context) : BaseDatastoreApiProvider() {
    private val Context.dataStore by preferencesDataStore(name = SETTINGS_PREFS)

    override val dataStore: DataStore<Preferences>
        get() = context.dataStore

    companion object {
        private const val SETTINGS_PREFS = "settings"
    }
}