package site.smartenglish.manager

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

@Singleton
class DataStoreManager @Inject constructor(
    private val context: Context
) {
    companion object {
        val TOKEN_KEY = stringPreferencesKey("jwt")
        val LEARN_NUM = intPreferencesKey("learn_num")
    }

    init {
        runBlocking {
            cachedToken = context.dataStore.data.map { preferences ->
                preferences[TOKEN_KEY]
            }.first()
            Log.d("DataStoreManager", "Cached token initialized: $cachedToken")
            learnNum = context.dataStore.data.map { preferences ->
                preferences[LEARN_NUM]
            }.first()
            Log.d("DataStoreManager", "Cached learnNum initialized: $learnNum")
        }
    }

    private var cachedToken: String? = null
    private var learnNum: Int? = null

    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
        cachedToken = token
    }

    fun getTokenSync(): String? = cachedToken

    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
        cachedToken = null
    }

    // 保存学习单词数量
    suspend fun saveLearnWordsCount(count: Int) {
        context.dataStore.edit { preferences ->
            preferences[LEARN_NUM] = count
        }
        learnNum = count
    }

    // 获取学习单词数量，默认为10
    fun getLearnWordsCountSync(): Int {
        return learnNum ?: 10
    }
}