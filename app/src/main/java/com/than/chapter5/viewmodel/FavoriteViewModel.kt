package com.than.chapter5.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.than.chapter5.datastore.DataStoreManager
import com.than.chapter5.model.User

class FavoriteViewModel(private val pref: DataStoreManager): ViewModel() {
    fun getDataUser(): LiveData<User> {
        return pref.getUser().asLiveData()
    }
}