package com.developerxy.wildlegion.screens.main.network

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

private const val BASE_URL_KEY = "baseUrl"

@Module
class RetrofitModule {

    @Named(BASE_URL_KEY)
    @Provides
    fun provideBaseUrl() = "http://wildlegion.wixsite.com/wildlegion/_functions/"

    @Singleton
    @Provides
    fun provideClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.HEADERS

        return OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(@Named(BASE_URL_KEY) baseUrl: String, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    @Singleton
    @Provides
    fun provideWixAPI(retrofit: Retrofit): WixAPI {
        return retrofit.create<WixAPI>(WixAPI::class.java)
    }
}