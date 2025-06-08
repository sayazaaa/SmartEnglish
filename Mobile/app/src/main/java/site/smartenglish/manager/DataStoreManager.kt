package site.smartenglish.manager

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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
        val TODAY_LEARN_NUM = intPreferencesKey("today_learn_num")
        val TODAY_LEARN_TIME = intPreferencesKey("today_learn_time")
        val LAST_DATE = stringPreferencesKey("last_date") // 新增：存储最后学习日期的Key
    }

    private val dataStoreFlow = context.dataStore.data
    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        // 初始化时开始收集Flow更新缓存
        scope.launch {
            dataStoreFlow.collect { preferences ->
                preferences[TOKEN_KEY]?.also { cachedToken = it }
                preferences[LEARN_NUM]?.also { learnNum = it }
                preferences[TODAY_LEARN_NUM]?.also { todayLearnNum = it }
                preferences[TODAY_LEARN_TIME]?.also { todayLearnTime = it }
                preferences[LAST_DATE]?.also { lastDate = it }
            }
        }

        // 阻塞初始化缓存
        runBlocking {
            cachedToken = context.dataStore.data.map { it[TOKEN_KEY] }.first()
            learnNum = context.dataStore.data.map { it[LEARN_NUM] }.first()
            todayLearnNum = context.dataStore.data.map { it[TODAY_LEARN_NUM] }.first()
            todayLearnTime = context.dataStore.data.map { it[TODAY_LEARN_TIME] }.first()
            lastDate = context.dataStore.data.map { it[LAST_DATE] }.first()

            Log.d("DataStoreManager", "初始化完成: 日期=$lastDate")
        }
    }

    private var cachedToken: String? = null
    private var learnNum: Int? = null
    private var todayLearnNum: Int? = null
    private var todayLearnTime: Int? = null
    private var lastDate: String? = null // 新增：最后学习日期缓存

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

    suspend fun saveLearnWordsCount(count: Int) {
        context.dataStore.edit { preferences ->
            preferences[LEARN_NUM] = count
        }
        learnNum = count
    }

    fun getLearnWordsCountSync(): Int {
        return learnNum ?: 10
    }

    suspend fun addTodayLearnNum(count: Int) {
        checkAndResetDate()
        context.dataStore.edit { preferences ->
            preferences[TODAY_LEARN_NUM] = count + (preferences[TODAY_LEARN_NUM] ?: 0)
        }
        todayLearnNum = (todayLearnNum ?: 0) + count
    }

    suspend fun getTodayLearnNumSync(): Int {
        checkAndResetDate()
        return todayLearnNum ?: 0
    }

    suspend fun addTodayLearnTime(time: Int) {
        checkAndResetDate()
        context.dataStore.edit { preferences ->
            preferences[TODAY_LEARN_TIME] = time + (preferences[TODAY_LEARN_TIME] ?: 0)
        }
        todayLearnTime = (todayLearnTime ?: 0) + time
    }

    suspend fun getTodayLearnTimeSync(): Int {
        checkAndResetDate()
        return todayLearnTime ?: 0
    }

    // 新增：日期检查和重置逻辑
    private suspend fun checkAndResetDate() {
        val today = getTodayDateString()
        // 日期发生变化（新的一天）
        if (lastDate != today) {
            Log.d("DataStoreManager", "检测到新日期: $lastDate -> $today, 重置今日数据")
            context.dataStore.edit { preferences ->
                preferences[TODAY_LEARN_NUM] = 0
                preferences[TODAY_LEARN_TIME] = 0
                preferences[LAST_DATE] = today
            }
            // 更新缓存
            todayLearnNum = 0
            todayLearnTime = 0
            lastDate = today
        }
    }

    // 新增：获取当前日期字符串 (格式: yyyyMMdd)
    private fun getTodayDateString(): String {
        return LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)
    }
}