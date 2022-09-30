package com.bartozo.lifeprogress.db

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.bartozo.lifeprogress.data.AppTheme
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.time.LocalDate
import javax.inject.Inject

private const val STORE_NAME = "life_progress_data_store"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = STORE_NAME)

class PrefsStoreImpl @Inject constructor(
    @ApplicationContext appContext: Context
) : PrefsStore {

    private object PreferencesKeys {
        val BIRTH_DAY_KEY = longPreferencesKey("birth_day")
        val LIFE_EXPECTANCY_KEY = intPreferencesKey("life_expectancy")
        val DID_SEE_WELCOME_KEY = booleanPreferencesKey("did_see_welcome")
        val APP_THEME_KEY = stringPreferencesKey("app_theme")
    }

    private val dataStore = appContext.dataStore

    override fun birthDayFlow() = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map {
        it[PreferencesKeys.BIRTH_DAY_KEY]?.let { epoch ->
            LocalDate.ofEpochDay(epoch)
        } ?: LocalDate.now()
    }

    override fun lifeExpectancyFlow() = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { it[PreferencesKeys.LIFE_EXPECTANCY_KEY] ?: 90 }

    override fun didSeeWelcomeFlow() = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { it[PreferencesKeys.DID_SEE_WELCOME_KEY] ?: false }

    override fun appThemeFlow() = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map {
        val savedTheme = it[PreferencesKeys.APP_THEME_KEY]
        if (savedTheme != null) {
            AppTheme.valueOf(savedTheme)
        } else {
            AppTheme.SYSTEM_AUTO
        }
    }

    override suspend fun saveBirthDay(birthDay: LocalDate) {
        val birthDayInDays = birthDay.toEpochDay()

        dataStore.edit {
            it[PreferencesKeys.BIRTH_DAY_KEY] = birthDayInDays
        }
    }

    override suspend fun saveLifeExpectancy(lifeExpectancy: Int) {
        dataStore.edit {
            it[PreferencesKeys.LIFE_EXPECTANCY_KEY] = lifeExpectancy
        }
    }

    override suspend fun updatedDidSeeWelcome(didSeeWelcome: Boolean) {
        dataStore.edit {
            it[PreferencesKeys.DID_SEE_WELCOME_KEY] = didSeeWelcome
        }
    }

    override suspend fun updateAppTheme(appTheme: AppTheme) {
        dataStore.edit {
            it[PreferencesKeys.APP_THEME_KEY] = appTheme.toString()
        }
    }
}