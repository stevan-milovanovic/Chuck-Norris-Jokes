package rs.smobile.chucknorrisjokes.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import rs.smobile.chucknorrisjokes.data.api.ApiConstants.API_KEY_HEADER_NAME
import rs.smobile.chucknorrisjokes.data.api.ApiConstants.API_KEY_HEADER_VALUE
import rs.smobile.chucknorrisjokes.data.api.ApiConstants.BASE_URL
import rs.smobile.chucknorrisjokes.data.api.ApiConstants.CONTENT_VALUE_HEADER_NAME
import rs.smobile.chucknorrisjokes.data.api.ApiConstants.CONTENT_VALUE_HEADER_VALUE
import rs.smobile.chucknorrisjokes.data.api.ApiConstants.HOST_HEADER_NAME
import rs.smobile.chucknorrisjokes.data.api.ApiConstants.HOST_HEADER_VALUE
import rs.smobile.chucknorrisjokes.data.api.JokeApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object JokeApiModule {

    @Provides
    @Singleton
    fun provideJokeApi(builder: Retrofit.Builder): JokeApi {
        return builder
            .build()
            .create(JokeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofitBuilder(client: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(headerInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder().apply {
            interceptors().add(headerInterceptor)
        }.build()
    }

    @Provides
    @Singleton
    fun provideHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            requestBuilder.header(HOST_HEADER_NAME, HOST_HEADER_VALUE)
            requestBuilder.header(API_KEY_HEADER_NAME, API_KEY_HEADER_VALUE)
            requestBuilder.header(CONTENT_VALUE_HEADER_NAME, CONTENT_VALUE_HEADER_VALUE)
            return@Interceptor chain.proceed(requestBuilder.build())
        }
    }

}