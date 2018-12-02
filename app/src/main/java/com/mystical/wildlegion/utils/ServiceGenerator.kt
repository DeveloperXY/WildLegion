package com.mystical.wildlegion.utils

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ServiceGenerator {
    companion object {
        private val BASE_URL = "http://wildlegion.wixsite.com/wildlegion/_functions/"

        private val builder = Retrofit.Builder().baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
        private var retrofit = builder.build()
        private val logging = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        private val httpClient = OkHttpClient.Builder()

        private val availableServices = mutableListOf<Any>()

        /**
         * @param serviceClass the class type of the client to be generated
         * @param <S> the type of client to be generated
         * @return an instance of the requested client that is ready for use.
        </S> */
        fun <S> createService(serviceClass: Class<S>): S {
            for (service in availableServices) {
                if (serviceClass.isAssignableFrom(service.javaClass))
                    return service as S
            }
            if (!httpClient.interceptors().contains(logging)) {
                httpClient.addInterceptor(logging)
                builder.client(httpClient.build())
                retrofit = builder.build()
            }

            val service = retrofit.create(serviceClass)
            availableServices.add(serviceClass)
            return service
        }
    }
}