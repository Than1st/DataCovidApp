package com.than.chapter5.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.github.dhaval2404.imagepicker.ImagePicker
import com.than.chapter5.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {
    companion object {
        private const val DATASTORE_NAME = "user_preferences"
        private val ID_USER_KEY = intPreferencesKey("id_user_key")
        private val NAMA_KEY = stringPreferencesKey("nama_key")
        private val EMAIL_KEY = stringPreferencesKey("email_key")
        private val USERNAME_KEY = stringPreferencesKey("username_key")
        private val PASSWORD_KEY = stringPreferencesKey("password_key")
        private val IMAGE_KEY = stringPreferencesKey("image_key")
        val Context.userDataStore by preferencesDataStore(DATASTORE_NAME)
    }
    suspend fun setUser(user: User){
        context.userDataStore.edit { preferences ->
            preferences[ID_USER_KEY] = user.id_user!!.toInt()
            preferences[NAMA_KEY] = user.nama
            preferences[EMAIL_KEY] = user.email
            preferences[USERNAME_KEY] = user.username
            preferences[PASSWORD_KEY] = user.password
            preferences[IMAGE_KEY] = user.image
        }
    }
    fun getUser(): Flow<User> {
        return context.userDataStore.data.map { preferences ->
            User(
                preferences[ID_USER_KEY] ?: -1,
                preferences[NAMA_KEY] ?: "default_nama",
                preferences[EMAIL_KEY] ?: "default_email@gmail.com",
                preferences[USERNAME_KEY] ?: "default_username",
                preferences[PASSWORD_KEY] ?: "default_password",
                preferences[IMAGE_KEY] ?: "no_image"
            )
        }
    }
    suspend fun deleteUser(){
        context.userDataStore.edit {
            it.clear()
        }
    }
}