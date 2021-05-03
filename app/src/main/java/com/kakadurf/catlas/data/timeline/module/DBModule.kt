package com.kakadurf.catlas.data.timeline.module

import android.content.Context
import com.kakadurf.catlas.data.timeline.db.CachingDB
import com.kakadurf.catlas.data.timeline.db.DBCacheDao
import com.kakadurf.catlas.presentation.general.di.annotation.AppScope
import dagger.Module
import dagger.Provides

@Module
class DBModule {
    @AppScope
    @Provides
    fun getDataBase(context: Context) = CachingDB.create(context)

    @Provides
    @AppScope
    fun getDao(db: CachingDB): DBCacheDao = db.getDao()
}
