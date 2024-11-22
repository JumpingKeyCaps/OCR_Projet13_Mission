package com.openclassrooms.hexagonal.games.screen.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// Création du DataStore
val Context.dataStore by preferencesDataStore(name = "user_preferences")

/**
 *  Preference manager for the app notifications.
 */
class PreferenceManager @Inject constructor(@ApplicationContext context: Context) {

    // Accéder à la DataStore
    private val dataStore = context.dataStore

    // Clé pour stocker l'état des notifications
    private object PreferencesKeys {
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
    }

    // Flow pour observer l'état des notifications
    val notificationsEnabledFlow: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] ?: true // Par défaut activé
        }

    // Modifier l'état des notifications
    suspend fun setNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] = enabled
        }
    }
}