package com.than.chapter5.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.than.chapter5.datastore.DataStoreManager
import com.than.chapter5.model.User
import kotlinx.coroutines.flow.toList

class HomeViewModel(private val pref: DataStoreManager): ViewModel() {
//    var dataUser : MutableLiveData<User> = MutableLiveData()

    suspend fun setDataUser(user: User){
//        dataUser.postValue(user)
        pref.setUser(user)
    }

    fun getDataUser(): LiveData<User>{
        return pref.getUser().asLiveData()
    }
}