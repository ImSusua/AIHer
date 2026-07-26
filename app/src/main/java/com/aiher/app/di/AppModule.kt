package com.aiher.app.di

import android.content.Context
import androidx.room.Room
import com.aiher.app.data.local.AppDatabase
import com.aiher.app.data.local.SettingsDataStore
import com.aiher.app.data.remote.AIChatApi
import com.aiher.app.data.remote.AuthApi
import com.aiher.app.data.remote.MarketApi
import com.aiher.app.data.repository.AIChatRepository
import com.aiher.app.data.repository.MarketRepository
import com.aiher.app.data.repository.ProjectRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideAIChatApi(okHttpClient: OkHttpClient): AIChatApi {
        return Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AIChatApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMarketApi(okHttpClient: OkHttpClient): MarketApi {
        return Retrofit.Builder()
            .baseUrl("https://api.aibox.asia/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MarketApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthApi(okHttpClient: OkHttpClient): AuthApi {
        return Retrofit.Builder()
            .baseUrl("https://api.aibox.asia/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "aiher_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideSettingsDataStore(@ApplicationContext context: Context): SettingsDataStore {
        return SettingsDataStore(context)
    }

    @Provides
    @Singleton
    fun provideProjectDao(database: AppDatabase) = database.projectDao()

    @Provides
    @Singleton
    fun provideMessageDao(database: AppDatabase) = database.messageDao()

    @Provides
    @Singleton
    fun provideAppDao(database: AppDatabase) = database.appDao()

    @Provides
    @Singleton
    fun provideAIChatRepository(
        api: AIChatApi,
        messageDao: com.aiher.app.data.local.MessageDao,
        appDao: com.aiher.app.data.local.AppDao,
        settingsDataStore: SettingsDataStore
    ): AIChatRepository {
        return AIChatRepository(api, messageDao, appDao, settingsDataStore)
    }

    @Provides
    @Singleton
    fun provideProjectRepository(
        projectDao: com.aiher.app.data.local.ProjectDao,
        appDao: com.aiher.app.data.local.AppDao
    ): ProjectRepository {
        return ProjectRepository(projectDao, appDao)
    }

    @Provides
    @Singleton
    fun provideMarketRepository(): MarketRepository {
        return MarketRepository()
    }
}