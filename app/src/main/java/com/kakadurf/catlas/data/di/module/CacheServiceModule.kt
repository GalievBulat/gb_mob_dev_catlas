package com.kakadurf.catlas.data.di.module

import com.kakadurf.catlas.data.CachingService
import com.kakadurf.catlas.data.DBCachingService
import dagger.Binds
import dagger.Module

@Module
abstract class CacheServiceModule {
    @Binds
    abstract fun getCachingService(cachingService: DBCachingService): CachingService
}