package com.example.caritaappnew.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class userPreference private constructor(private val dataStore: DataStore<Preferences>) {
    companion object {
        @Volatile
        private var INSTANCE: userPreference? = null
        private val name_key = stringPreferencesKey("name_user")
        private val email_key = stringPreferencesKey("email_user")
        private val password_key = stringPreferencesKey("password_user")
        private val id_key = stringPreferencesKey("id_user")
        private val token_key = stringPreferencesKey("token_user")
        private val islogin_key = booleanPreferencesKey("islogin_user")

        fun getInstance(dataStore: DataStore<Preferences>): userPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = userPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }



    fun getData(): Flow<UserModel> {
        return dataStore.data.map {
            UserModel(
                it[name_key] ?: "",
                it[email_key] ?: "",
                it[password_key] ?: "",
                it[id_key] ?: "",
                it[token_key] ?: "",
                it[islogin_key] ?: false
            )
        }
    }

    suspend fun saveData(Data : UserModel){
        dataStore.edit {
            it[name_key] = Data.name
            it[email_key] = Data.email
            it[password_key] = Data.password
            it[id_key] = Data.userId
            it[token_key] = Data.token
            it[islogin_key] = Data.isLogin
        }
    }

    suspend fun logoutUser(){
        dataStore.edit {
            it[name_key] = ""
            it[email_key] = ""
            it[password_key] = ""
            it[id_key] = ""
            it[token_key] = ""
            it[islogin_key] = false
        }
    }
}