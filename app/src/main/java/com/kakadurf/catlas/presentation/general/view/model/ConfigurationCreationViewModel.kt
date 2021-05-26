package com.kakadurf.catlas.presentation.general.view.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakadurf.catlas.data.timeline.db.CachedConfiguration
import com.kakadurf.catlas.data.timeline.db.DBConfigDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ConfigurationCreationViewModel : ViewModel() {
    @Inject
    lateinit var dao: DBConfigDao
    private val mContextNames: MutableLiveData<List<String>> = MutableLiveData()

    val contextNames: LiveData<List<String>> = mContextNames

    fun getConfigGroups() {
        viewModelScope.launch(Dispatchers.IO) {
            mContextNames.postValue(
                dao.getAllContextGroups()
            )
        }
    }

    fun saveConfig(article: String, name: String, group: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.saveConfigToDB(
                CachedConfiguration(
                    name, article, group
                )
            )
        }
    }
}
