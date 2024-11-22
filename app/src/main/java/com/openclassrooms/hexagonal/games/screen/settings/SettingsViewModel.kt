package com.openclassrooms.hexagonal.games.screen.settings

import android.app.NotificationManager
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing user settings, specifically notification preferences.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
  private val preferenceManager: PreferenceManager,
  @ApplicationContext private val context: Context // Pour NotificationManager
) : ViewModel() {

  private val notificationManager =
    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

  // Flow exposé pour observer l'état des notifications
  val areNotificationsEnabled: Flow<Boolean> = preferenceManager.notificationsEnabledFlow


  // Activer les notifications
  fun enableNotifications() {
    viewModelScope.launch {
      preferenceManager.setNotificationsEnabled(true)
      if (notificationManager.isNotificationPolicyAccessGranted) {
        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
      }
    }
  }

  // Désactiver les notifications
  fun disableNotifications() {
    viewModelScope.launch {
      preferenceManager.setNotificationsEnabled(false)
      if (notificationManager.isNotificationPolicyAccessGranted) {
        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE)
      }
    }
  }

}