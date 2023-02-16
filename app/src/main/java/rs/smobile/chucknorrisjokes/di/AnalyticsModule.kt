package rs.smobile.chucknorrisjokes.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import rs.smobile.chucknorrisjokes.analytics.AnalyticsService
import rs.smobile.chucknorrisjokes.analytics.AppCenterAnalyticsService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {

    @Provides
    @Singleton
    fun provideAnalyticsService(): AnalyticsService {
        return AppCenterAnalyticsService()
    }

}