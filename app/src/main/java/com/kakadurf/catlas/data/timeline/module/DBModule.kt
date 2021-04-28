package com.kakadurf.catlas.data.timeline.module

import android.content.Context
import com.kakadurf.catlas.data.timeline.db.CachingDB
import com.kakadurf.catlas.data.timeline.db.DBCacheDao
import com.kakadurf.catlas.presentation.general.di.annotation.DBScope
import dagger.Module
import dagger.Provides

@Module
class DBModule {
    @DBScope
    @Provides
    fun getDataBase(context: Context) = CachingDB.create(context)

    @Provides
    @DBScope
    fun getDao(db: CachingDB): DBCacheDao = db.getDao()
}
