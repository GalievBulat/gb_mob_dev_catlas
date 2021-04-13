package com.kakadurf.catlas.data.di.module

import android.content.Context
import com.kakadurf.catlas.data.db.CachingDB
import com.kakadurf.catlas.data.db.DBCacheDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DBModule {
    @Singleton
    @Provides
    fun getDataBase(context: Context) = CachingDB.create(context)

    @Provides
    @Singleton
    fun getDao(db: CachingDB): DBCacheDao = db.getDao()
}